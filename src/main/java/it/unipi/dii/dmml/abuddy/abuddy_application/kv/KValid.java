package it.unipi.dii.dmml.abuddy.abuddy_application.kv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import weka.clusterers.NumberOfClustersRequestable;
import weka.clusterers.RandomizableClusterer;
import weka.clusterers.SimpleKMeans;
//import it.unipi.dii.dmml.abuddy.abuddy_application.kv.kvalid.GraphPlotter;
import it.unipi.dii.dmml.abuddy.abuddy_application.kv.kvalid.SilhouetteIndex;
import weka.core.Capabilities;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.core.SelectedTag;
import weka.core.Tag;
import weka.core.Utils;
import weka.core.WeightedInstancesHandler;
import weka.core.Capabilities.Capability;

public class KValid extends RandomizableClusterer implements NumberOfClustersRequestable, WeightedInstancesHandler {
    static final long serialVersionUID = -206633168493633341L;
    private SimpleKMeans m_skmeans;
    private int m_numClusters = 3;
    private DistanceFunction m_distanceFunction = new EuclideanDistance();
    private int m_maxInteration = 500;
    public static final int RANDOM = 0;
    public static final int KMEANS_PLUS_PLUS = 1;
    public static final int CANOPY = 2;
    public static final int FARTHEST_FIRST = 3;
    public static final int SILHOUETTE_INDEX = 0;
    public static final int ELBOW_METHOD = 1;
    protected int m_validationMethod = 0;
    public static final Tag[] VALIDATION_SELECTION = new Tag[]{new Tag(0, "Silhouette Index"), new Tag(1, "Elbow method")};
    protected int m_initializationMethod = 0;
    protected Instances m_instances = null;
    protected int m_minimumK = 3;
    protected int m_maximumK = 10;
    protected boolean m_cascade = false;
    protected ArrayList<SilhouetteIndex> m_silhouetteIdx;
    protected ArrayList<Double> m_elbow;
    protected int m_bestK = 0;
    protected boolean m_showGraph = false;

    public KValid() {
        this.m_SeedDefault = 10;
        this.setSeed(this.m_SeedDefault);
    }

    public String globalInfo() {
        return "KValid: SimpleKMeans with cluster validation. More information, visit https://github.com/Theldus/KValid";
    }

    public Capabilities getCapabilities() {
        Capabilities var1 = super.getCapabilities();
        var1.disableAll();
        var1.enable(Capability.NO_CLASS);
        var1.enable(Capability.NOMINAL_ATTRIBUTES);
        var1.enable(Capability.NUMERIC_ATTRIBUTES);
        var1.enable(Capability.MISSING_VALUES);
        return var1;
    }

    public void buildClusterer(Instances var1) throws Exception {
        int var2 = this.m_numClusters;
        int var3 = this.m_numClusters;
        this.m_instances = var1;
        if (this.m_cascade) {
            if (this.m_minimumK >= this.m_maximumK || this.m_minimumK < 2 || this.m_maximumK < 3) {
                throw new Exception("Wrong minimum/maximum values, minimum should be >= 2 and maximum >= 3");
            }

            var2 = this.m_minimumK;
            var3 = this.m_maximumK;
        }

        this.m_silhouetteIdx = new ArrayList();
        this.m_elbow = new ArrayList();

        for(int var4 = var2; var4 <= var3; ++var4) {
            this.m_skmeans = new SimpleKMeans();
            this.m_skmeans.setInitializationMethod(new SelectedTag(this.m_initializationMethod, SimpleKMeans.TAGS_SELECTION));
            this.m_skmeans.setSeed(this.m_SeedDefault);
            this.m_skmeans.setNumClusters(var4);
            this.m_skmeans.setDistanceFunction(this.m_distanceFunction);
            this.m_skmeans.setMaxIterations(this.m_maxInteration);
            //keep order of instances
            this.m_skmeans.setPreserveInstancesOrder(true);
            this.m_skmeans.buildClusterer(var1);
            if (this.m_validationMethod == 0) {
                this.m_silhouetteIdx.add(new SilhouetteIndex());
                ((SilhouetteIndex)this.m_silhouetteIdx.get(var4 - var2)).evaluate(this.m_skmeans, this.m_skmeans.getClusterCentroids(), this.m_instances, this.m_distanceFunction);
            } else if (this.m_validationMethod == 1) {
                this.m_elbow.add(this.m_skmeans.getSquaredError());
            }
        }

        if (this.m_cascade) {
            int var6;
            double var7;
            if (this.m_validationMethod == 0) {
                var7 = 0.0D;

                for(var6 = 0; var6 < this.m_silhouetteIdx.size(); ++var6) {
                    if (((SilhouetteIndex)this.m_silhouetteIdx.get(var6)).getGlobalSilhouette() > var7) {
                        var7 = ((SilhouetteIndex)this.m_silhouetteIdx.get(var6)).getGlobalSilhouette();
                        this.m_bestK = var6;
                    }
                }
            } else if (this.m_validationMethod == 1) {
                var7 = 0.0D;

                for(var6 = 0; var6 < this.m_elbow.size(); ++var6) {
                    if ((Double)this.m_elbow.get(var6) > var7) {
                        var7 = (Double)this.m_elbow.get(var6);
                        this.m_bestK = var6;
                    }
                }
            }

            this.m_bestK += var2;
            this.m_skmeans = new SimpleKMeans();
            this.m_skmeans.setInitializationMethod(new SelectedTag(this.m_initializationMethod, SimpleKMeans.TAGS_SELECTION));
            this.m_skmeans.setSeed(this.m_SeedDefault);
            this.m_skmeans.setNumClusters(this.m_bestK);
            this.m_skmeans.setDistanceFunction(this.m_distanceFunction);
            this.m_skmeans.setMaxIterations(this.m_maxInteration);
            this.m_skmeans.buildClusterer(var1);
            this.setNumClusters(this.m_bestK);
        }

    }

    public int clusterInstance(Instance var1) throws Exception {
        if (this.m_skmeans == null) {
            throw new Exception("The clusterer was not build yet!");
        } else {
            return this.m_skmeans.clusterInstance(var1);
        }
    }

    public String initializationMethodTipText() {
        return "The initialization method to use.";
    }

    public SelectedTag getInitializationMethod() {
        return new SelectedTag(this.m_initializationMethod, SimpleKMeans.TAGS_SELECTION);
    }

    public void setInitializationMethod(SelectedTag var1) {
        if (var1.getTags() == SimpleKMeans.TAGS_SELECTION) {
            this.m_initializationMethod = var1.getSelectedTag().getID();
        }

    }

    public String validationMethodTipText() {
        return "Which validation method: Silhouette Index or Elbow method";
    }

    public SelectedTag getValidationMethod() {
        return new SelectedTag(this.m_validationMethod, VALIDATION_SELECTION);
    }

    public void setValidationMethod(SelectedTag var1) {
        if (var1.getTags() == VALIDATION_SELECTION) {
            this.m_validationMethod = var1.getSelectedTag().getID();
        }

    }

    public int numberOfClusters() throws Exception {
        return this.m_numClusters;
    }

    public String numClustersTipText() {
        return "Set the number of clusters";
    }

    public int getNumClusters() {
        return this.m_numClusters;
    }

    public void setNumClusters(int var1) throws Exception {
        if (var1 <= 0) {
            throw new Exception("Number of clusters must be > 0");
        } else {
            this.m_numClusters = var1;
        }
    }

    public String maxIterationsTipText() {
        return "Set the maximum number of iterations";
    }

    public int getMaxIterations() {
        return this.m_maxInteration;
    }

    public void setMaxIterations(int var1) throws Exception {
        if (var1 < 1) {
            throw new Exception("Number of iterations should be > 1");
        } else {
            this.m_maxInteration = var1;
        }
    }

    public String distanceFunctionTipText() {
        return "The distance function to use for comparison";
    }

    public DistanceFunction getDistanceFunction() {
        return this.m_distanceFunction;
    }

    public void setDistanceFunction(DistanceFunction var1) throws Exception {
        if (!(var1 instanceof EuclideanDistance) && !(var1 instanceof ManhattanDistance)) {
            throw new Exception("KValid currently only supports the Euclidean and Manhattan distances.");
        } else {
            this.m_distanceFunction = var1;
        }
    }

    public String minimumKTipText() {
        return "The minimum K value for test when cascade option is enabled";
    }

    public int getMinimumK() {
        return this.m_minimumK;
    }

    public void setMinimumK(int var1) throws Exception {
        this.m_minimumK = var1;
    }

    public String maximumKTipText() {
        return "The maximum K value for test when cascade option is enabled";
    }

    public int getMaximumK() {
        return this.m_maximumK;
    }

    public void setMaximumK(int var1) throws Exception {
        this.m_maximumK = var1;
    }

    public String cascadeTipText() {
        return "Cascade test: Tries to find the best K given a minimum/maximum value!";
    }

    public boolean getCascade() {
        return this.m_cascade;
    }

    public void setCascade(boolean var1) {
        this.m_cascade = var1;
    }

    public String setShowGraphTipText() {
        return "Show graph: shows the graph representing the results when in cascade mode!";
    }

    public boolean getShowGraph() {
        return this.m_showGraph;
    }

    public void setShowGraph(boolean var1) {
        this.m_showGraph = var1;
    }

    public String[] getOptions() {
        Vector var1 = new Vector();
        var1.add("-init");
        var1.add("" + this.getInitializationMethod().getSelectedTag().getID());
        var1.add("-N");
        var1.add("" + this.getNumClusters());
        var1.add("-A");
        var1.add((this.m_distanceFunction.getClass().getName() + " " + Utils.joinOptions(this.m_distanceFunction.getOptions())).trim());
        var1.add("-I");
        var1.add("" + this.getMaxIterations());
        var1.add("-validation");
        var1.add("" + this.getValidationMethod().getSelectedTag().getID());
        if (this.m_cascade) {
            var1.add("-cascade");
            var1.add("-minK");
            var1.add("" + this.getMinimumK());
            var1.add("-maxK");
            var1.add("" + this.getMaximumK());
        }

        if (this.m_showGraph) {
            var1.add("-show-graph");
        }

        Collections.addAll(var1, super.getOptions());
        return (String[])var1.toArray(new String[var1.size()]);
    }

    public void setOptions(String[] var1) throws Exception {
        String var2 = Utils.getOption("init", var1);
        if (var2.length() > 0) {
            this.setInitializationMethod(new SelectedTag(Integer.parseInt(var2), SimpleKMeans.TAGS_SELECTION));
        }

        var2 = Utils.getOption("N", var1);
        if (var2.length() > 0) {
            this.setNumClusters(Integer.parseInt(var2));
        }

        String var3 = Utils.getOption('A', var1);
        if (var3.length() != 0) {
            String[] var4 = Utils.splitOptions(var3);
            if (var4.length == 0) {
                throw new Exception("Invalid DistanceFunction specification string.");
            }

            String var5 = var4[0];
            var4[0] = "";
            this.setDistanceFunction((DistanceFunction)Utils.forName(DistanceFunction.class, var5, var4));
        } else {
            this.setDistanceFunction(new EuclideanDistance());
        }

        var2 = Utils.getOption("I", var1);
        if (var2.length() > 0) {
            this.setMaxIterations(Integer.parseInt(var2));
        }

        var2 = Utils.getOption("validation", var1);
        if (var2.length() > 0) {
            this.setValidationMethod(new SelectedTag(Integer.parseInt(var2), VALIDATION_SELECTION));
        }

        if (this.m_cascade = Utils.getFlag("cascade", var1)) {
            var2 = Utils.getOption("minK", var1);
            if (var2.length() > 0) {
                this.setMinimumK(Integer.parseInt(var2));
            }

            var2 = Utils.getOption("maxK", var1);
            if (var2.length() > 0) {
                this.setMaximumK(Integer.parseInt(var2));
            }
        }

        this.m_showGraph = Utils.getFlag("show-graph", var1);
        super.setOptions(var1);
        Utils.checkForRemainingOptions(var1);
    }

    public String toString() {
        if (this.m_skmeans == null) {
            return "I don't have any clusterer yet!";
        } else {
            StringBuffer var1 = new StringBuffer("KValid\n");
            var1.append("======\n\n");
            var1.append("=== Clustering validation, using: " + (this.m_validationMethod == 0 ? "Silhouette Index" : "Elbow method (SSE)") + " ===");
            int var2 = this.m_numClusters;
            int var3 = this.m_numClusters;
            if (this.m_cascade) {
                var2 = this.m_minimumK;
                var3 = this.m_maximumK;
            }

            var1.append("\n");
            int var4;
            int var5;
            ArrayList var6;
            //GraphPlotter var7;
            if (this.m_validationMethod == 0) {
                for(var4 = var2; var4 <= var3; ++var4) {
                    var1.append("\nFor k = " + var4 + "\n");
                    var1.append(((SilhouetteIndex)this.m_silhouetteIdx.get(var4 - var2)).toString() + "\n");
                }

                if (this.m_cascade) {
                    var1.append("\n~~ Best K: " + this.m_bestK + " ~~");
                    var1.append("\nPlease manually check your dataset to figure out if this is really the best K");
                    var6 = new ArrayList();

                    for(var5 = 0; var5 < this.m_silhouetteIdx.size(); ++var5) {
                        var6.add(((SilhouetteIndex)this.m_silhouetteIdx.get(var5)).getGlobalSilhouette());
                    }


                }
            } else if (this.m_validationMethod == 1) {
                for(var4 = var2; var4 <= var3; ++var4) {
                    var1.append("\nFor k = " + var4 + "\n");
                    var1.append("SSE: " + this.m_elbow.get(var4 - var2) + "\n");
                }

                if (this.m_cascade) {
                    var6 = new ArrayList();

                    for(var5 = 0; var5 < this.m_elbow.size(); ++var5) {
                        var6.add(this.m_elbow.get(var5));
                    }

                }
            }

            var1.append("\n\n");
            var1.append(this.m_skmeans.toString());
            return var1.toString();
        }
    }

    public static void main(String[] var0) {
        runClusterer(new it.unipi.dii.dmml.abuddy.abuddy_application.kv.KValid(), var0);
    }
}


