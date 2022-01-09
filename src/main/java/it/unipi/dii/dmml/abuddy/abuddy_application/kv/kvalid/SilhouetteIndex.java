package it.unipi.dii.dmml.abuddy.abuddy_application.kv.kvalid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import weka.clusterers.AbstractClusterer;
import it.unipi.dii.dmml.abuddy.abuddy_application.kv.kvalid.ClusterEvaluator;
import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;

public class SilhouetteIndex implements Serializable, ClusterEvaluator {
    static final long serialVersionUID = -305533168492651330L;
    protected ArrayList<Double> m_clustersSilhouette = new ArrayList();
    protected double m_globalSilhouette = 0.0D;

    public SilhouetteIndex() {
    }

    public void evaluate(AbstractClusterer var1, Instances var2, Instances var3, DistanceFunction var4) throws Exception {
        if (var1 != null && var3 != null) {
            ArrayList[] var5 = (ArrayList[])(new ArrayList[var2.size()]);

            int var6;
            for(var6 = 0; var6 < var2.size(); ++var6) {
                var5[var6] = new ArrayList();
            }

            for(var6 = 0; var6 < var3.size(); ++var6) {
                var5[var1.clusterInstance(var3.get(var6))].add(var3.get(var6));
            }

            for(var6 = 0; var6 < var5.length; ++var6) {
                double var7 = 0.0D;

                for(int var9 = 0; var9 < var5[var6].size(); ++var9) {
                    double var10 = 0.0D;
                    double var12 = 0.0D;
                    double var14 = 0.0D;
                    Instance var16 = (Instance)var5[var6].get(var9);

                    for(int var17 = 0; var17 < var5[var6].size(); ++var17) {
                        if (var17 != var9) {
                            Instance var18 = (Instance)var5[var6].get(var17);
                            var12 += var4.distance(var16, var18);
                        }
                    }

                    var12 /= (double)(var5[var6].size() - 1);
                    double var24 = 1.7976931348623157E308D;
                    int var19 = 0;

                    int var20;
                    Instance var21;
                    for(var20 = 0; var20 < var2.size(); ++var20) {
                        if (var20 != var6) {
                            var21 = var2.get(var20);
                            double var22 = var4.distance(var16, var21);
                            if (var22 < var24) {
                                var24 = var22;
                                var19 = var20;
                            }
                        }
                    }

                    for(var20 = 0; var20 < var5[var19].size(); ++var20) {
                        var21 = (Instance)var5[var19].get(var20);
                        var14 += var4.distance(var16, var21);
                    }

                    var14 /= (double)(var5[var19].size() - 1);
                    var10 = (var14 - var12) / Math.max(var12, var14);
                    var7 += var10;
                }

                var7 /= (double)(var5[var6].size() - 1);
                this.m_globalSilhouette += var7;
                this.m_clustersSilhouette.add(var7);
            }

            this.m_globalSilhouette /= (double)this.m_clustersSilhouette.size();
        } else {
            throw new Exception("SilhouetteIndex: the clusterer or instances are null!");
        }
    }

    public ArrayList<Double> getClustersSilhouette() {
        return this.m_clustersSilhouette;
    }

    public double getGlobalSilhouette() {
        return this.m_globalSilhouette;
    }

    public String evalSilhouette(double var1) {
        String var3 = "";
        if (var1 > 0.7D) {
            var3 = "strong structure!";
        } else if (var1 > 0.5D && var1 <= 0.7D) {
            var3 = "reasonably structure!";
        } else if (var1 > 0.25D && var1 <= 0.5D) {
            var3 = "weak structure!";
        } else if (var1 <= 0.25D) {
            var3 = "a non substancial structure was found!";
        }

        return var3;
    }

    public String toString() {
        StringBuffer var1 = new StringBuffer("");

        for(int var2 = 0; var2 < this.m_clustersSilhouette.size(); ++var2) {
            double var3 = (Double)this.m_clustersSilhouette.get(var2);
            var1.append("   Cluster " + var2 + ": " + String.format(Locale.US, "%.4f", var3) + ", veredict: " + this.evalSilhouette(var3) + "\n");
        }

        var1.append("   Mean: " + String.format(Locale.US, "%.4f", this.m_globalSilhouette) + ", veredict: " + this.evalSilhouette(this.m_globalSilhouette));
        return var1.toString();
    }
}

