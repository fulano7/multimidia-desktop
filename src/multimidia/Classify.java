package multimidia;

import weka.attributeSelection.AttributeSelection;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StringReader;
public class Classify {
	
	public static String classifySingleUnlabeledInstance(MusicInstance mi, String modelObjPath, String attributeSelectionObjPath) throws Exception{
		System.out.println(modelObjPath);
		System.out.println(attributeSelectionObjPath);
		Classifier classifier = (Classifier) read(Classify.class.getResourceAsStream(modelObjPath));
		Instances instances = new Instances(new StringReader(mi.toArffSingleInstanceFileValenceString()));
		// set class attribute
		instances.setClassIndex(instances.numAttributes() - 1);
		
		// performing attribute selection
		AttributeSelection as = (AttributeSelection) read(Classify.class.getResourceAsStream(attributeSelectionObjPath));
		instances = as.reduceDimensionality(instances);
		
		// label this single instance
		// clsLabel is 0.0 or 1.0
		double clsLabel = classifier.classifyInstance(instances.instance(0));
		instances.instance(0).setClassValue(clsLabel);
		return instances.instance(0).classAttribute().value((int) clsLabel);
	}
	
	public static String classifyActivationSingleUnlabeledInstance(MusicInstance mi, String modelObjPath, String attributeSelectionObjPath) throws Exception{
		System.out.println(modelObjPath);
		System.out.println(attributeSelectionObjPath);
		Classifier classifier = (Classifier) read(Classify.class.getResourceAsStream(modelObjPath));
		Instances instances = new Instances(new StringReader(mi.toArffSingleInstanceFileActivationString()));
		// set class attribute
		instances.setClassIndex(instances.numAttributes() - 1);
		// performing attribute selection
		AttributeSelection as = (AttributeSelection) read(Classify.class.getResourceAsStream(attributeSelectionObjPath));
		instances = as.reduceDimensionality(instances);
		
		// label this single instance
		// clsLabel is 0.0 or 1.0
		double clsLabel = classifier.classifyInstance(instances.instance(0));
		instances.instance(0).setClassValue(clsLabel);
		return instances.instance(0).classAttribute().value((int) clsLabel);
	}
	
	public static Object read(InputStream stream) throws Exception {
		if (!(stream instanceof BufferedInputStream)) {
			stream = new BufferedInputStream(stream);
		}
		ObjectInputStream ois = new ObjectInputStream(stream);
		Object result = ois.readObject();
		ois.close();

		return result;
	}
}


class MusicInstance{
	private double tempo, dance, speech, liveness, energy;
	private String valenceClass, activationClass;
	public MusicInstance(double tempo, double dance, double speech, double liveness, double energy){
		this.dance = dance;
		this.tempo = tempo;
		this.energy = energy;
		this.speech = speech;
		this.liveness = liveness;
		this.valenceClass = this.activationClass = "?";
	}
	
	
	public MusicInstance(double tempo, double dance, double speech, double liveness, double energy, String valenceClass, String activationClass){
		this(tempo, dance, speech, liveness, energy);
		this.valenceClass = valenceClass;
		this.activationClass = activationClass;
	}
	
	
	public String toArffSingleInstanceFileValenceString(){
		return "@relation whatever\n\n"+
				"@attribute Tempo numeric\n"+
				"@attribute Dance numeric\n"+
				"@attribute Speech numeric\n"+
				"@attribute Liveness numeric\n"+
				"@attribute Energy numeric\n"+
				"@attribute Valencia { triste , alegre } \n\n\n"+
				"@data\n"+
				this.toArffValenceString();
	}
	
	
	public String toArffValenceString(){
		return (this.tempo+" , "+this.dance+" , "+this.speech+" , "+this.liveness+" , "+this.energy+" , "+this.valenceClass+" \n");
	}
	
	
	public String toArffSingleInstanceFileActivationString(){
		return "@relation whatever\n\n"+
				"@attribute Tempo numeric\n"+
				"@attribute Dance numeric\n"+
				"@attribute Speech numeric\n"+
				"@attribute Liveness numeric\n"+
				"@attribute Energy numeric\n"+
				"@attribute Valencia { calmo , agitado } \n\n\n"+
				"@data\n"+
				this.toArffActivationString();
	}
	
	
	public String toArffActivationString(){
		return (this.tempo+" , "+this.dance+" , "+this.speech+" , "+this.liveness+" , "+this.energy+" , "+this.activationClass+" \n");
	}
	
	
	public void setValenceClass(String valenceClass){
		this.valenceClass = valenceClass;
	}
	
	
	public void setActivationClass(String activationClass){
		this.activationClass = activationClass;
	}
	
	
	public String getValenceClass(String valenceClass){
		return this.valenceClass;
	}
	
	public String getActivationClass(String activationClass){
		return this.activationClass;
	}
}