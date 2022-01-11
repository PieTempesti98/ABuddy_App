package it.unipi.dii.dmml.abuddy.abuddy_application.clustering;

import it.unipi.dii.dmml.abuddy.abuddy_application.kv.KValid;
import it.unipi.dii.dmml.abuddy.abuddy_application.utilities.DatabaseUtils;
import javafx.util.Pair;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.FilteredClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.SelectedTag;

import weka.filters.Filter;
import weka.filters.MultiFilter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.Remove;



import java.util.ArrayList;
import java.util.Enumeration;


public class KValid_Clusterer {

    private KValid kv;
    private Instances dataset;
    private ArrayList<String> username = new ArrayList<>();

    public int load_dataset() throws Exception {

        dataset = DatabaseUtils.getDataset();
        Enumeration<Object> values = dataset.attribute("username").enumerateValues();
        System.out.println(dataset.toString());
        while(values.hasMoreElements()){

            username.add((String) values.nextElement());

        }
        return username.size();
    }

    public FilteredClusterer cluster() throws Exception {
        //Definition of the pipelines
        FilteredClusterer filtered = new FilteredClusterer();
        MultiFilter multiFilter = new MultiFilter();

        //Filters setup
        Remove remove = new Remove();
        //remove.setInputFormat(dataset);
        remove.setAttributeIndices("1");

        Normalize normalize = new Normalize();
        //normalize.setInputFormat(dataset);
        normalize.setIgnoreClass(true);

        Filter[] filters = {remove, normalize};
        multiFilter.setInputFormat(dataset);
        multiFilter.setFilters(filters);

        //Clusterer setup
        kv = new KValid();
        kv.setMinimumK(2);
        kv.setMaximumK(100);
        kv.setCascade(true);
        kv.setInitializationMethod(new SelectedTag(SimpleKMeans.KMEANS_PLUS_PLUS,
                SimpleKMeans.TAGS_SELECTION));

        filtered.setFilter(multiFilter);
        filtered.setClusterer(kv);
        filtered.buildClusterer(dataset);

        ClusterEvaluation eval1 = new ClusterEvaluation();
        eval1.setClusterer(filtered);
        eval1.evaluateClusterer(dataset);
        System.out.println(eval1.clusterResultsToString());

        return filtered;
    }

    public ClusterEvaluation evaluateCluster(FilteredClusterer filtered){

        ClusterEvaluation eval = new ClusterEvaluation();
        eval.setClusterer(filtered);
        try {
            eval.evaluateClusterer(dataset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return eval;

    }

    public ArrayList<Pair> getAssignements(ClusterEvaluation eval){


        ArrayList<Pair> pairs = new ArrayList<>();
        double[] assignments =  eval.getClusterAssignments();
        for(int  i = 0; i < assignments.length; i++) {
            Pair p = new Pair(username.get(i),(int) assignments[i]);
            pairs.add(p);
        }

        return pairs;

    }



}
