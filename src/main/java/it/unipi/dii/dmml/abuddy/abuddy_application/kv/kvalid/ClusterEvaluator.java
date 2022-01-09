package it.unipi.dii.dmml.abuddy.abuddy_application.kv.kvalid;

import weka.clusterers.AbstractClusterer;
import weka.core.DistanceFunction;
import weka.core.Instances;

public interface ClusterEvaluator {
    void evaluate(AbstractClusterer var1, Instances var2, Instances var3, DistanceFunction var4) throws Exception;
}
