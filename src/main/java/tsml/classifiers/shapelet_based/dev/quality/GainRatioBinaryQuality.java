package tsml.classifiers.shapelet_based.dev.quality;

import tsml.classifiers.shapelet_based.dev.distances.ShapeletDistanceFunction;
import tsml.classifiers.shapelet_based.dev.type.ShapeletMV;
import tsml.data_containers.TimeSeriesInstances;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;

public class GainRatioBinaryQuality extends ShapeletQualityFunction {

    public GainRatioBinaryQuality(TimeSeriesInstances instances,
                                  ShapeletDistanceFunction distance){
        super();
        this.trainInstances = instances;
        this.classIndexes = instances.getClassIndexes();
        this.classNames = instances.getClassLabels();
        this.classCounts = instances.getClassCounts();
        this.distance = distance;

        this.atts = new ArrayList<Attribute>(2);


        this.atts.add(new Attribute("distance"));

      //  FastVector my_nominal_values = new FastVector(2);
      //  my_nominal_values.addElement("positive");
      //  my_nominal_values.addElement("negative");

       // this.atts.add(new Attribute("class",my_nominal_values));
        this.atts.add(new Attribute("class", Arrays.asList(new String[]{"0","1"})));
    }

    @Override
    public double calculate(ShapeletMV candidate) {

        this.instances = new Instances("TestInstances",atts,0);
        this.instances.setClassIndex(1);

        double cl = 0;
        for (int i=0;i< trainInstances.numInstances();i++){
            if (classIndexes[i]==candidate.getClassIndex()){
                cl = 1;
            }else{
                cl = 0;
            }
            double dist = distance.calculate(candidate,trainInstances.get(i));
            instances.add(new DenseInstance(1,new double[]{dist,cl}));

        }
       // System.out.println(instances);

        GainRatioAttributeEval eval = new   GainRatioAttributeEval();
            try {
                eval.buildEvaluator(instances);
                return eval.evaluateAttribute(0);
            } catch (Exception e) {
                e.printStackTrace();

            }

            return 0;


    }


}