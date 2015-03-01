package multimidia;

import weka.attributeSelection.AttributeSelection;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
public class Classify {
	/**
	 * Classifica um conjunto de instancias, sem salvar predictions,
	 * assumindo que queremos inferir o ultimo atributo
	 * @param attributeSelectionObjPath caminho pra arquivo que armazena uma instancia da classe de selecao de atributos.
	 * @param modelObjPath caminho pra arquivo que armazena o modelo treinado (classificador)
	 * @param instancesPath caminho para o arquivo arff com as instancias
	 * @return Uma string com os resultados da avaliacao
	 * @throws Exception
	 */
	
	public static String classifyInstances(String attributeSelectionObjPath, String modelObjPath, String instancesPath) throws Exception{
		DataSource dataSource = new DataSource(new FileInputStream(instancesPath));
		Instances instances = dataSource.getDataSet();
		// O ultimo atributo eh o q queremos inferir
		instances.setClassIndex(instances.numAttributes() - 1);
		//Carregar a classe de selecao de atributos, se necessario
		
		if(attributeSelectionObjPath != null){
			AttributeSelection as = (AttributeSelection)weka.core.SerializationHelper.read(attributeSelectionObjPath);
			instances = as.reduceDimensionality(instances);
		}
		
		//Carregar classificador
		Classifier classifier = (Classifier)weka.core.SerializationHelper.read(modelObjPath);
		//Fazer a avaliacao
		Evaluation eval = new Evaluation(instances);
		// terceiro parametro eh obrigatorio, por qneuanto nao vamos usa-lo
		Object[] nul = new Object[0];
		eval.evaluateModel(classifier, instances, nul);
		return eval.toSummaryString();
	}
	
	
	public static String classifySingleUnlabeledInstance(MusicInstance mi, String modelObjPath, String attributeSelectionObjPath) throws Exception{
		Classifier classifier = (Classifier)weka.core.SerializationHelper.read(modelObjPath);
		Instances instances = new Instances(new StringReader(mi.toArffSingleInstanceFileValenceString()));
		// set class attribute
		instances.setClassIndex(instances.numAttributes() - 1);
		// performing attribute selection
		
		if(new File(attributeSelectionObjPath).exists()){
			AttributeSelection as = (AttributeSelection) weka.core.SerializationHelper.read(attributeSelectionObjPath);
			instances = as.reduceDimensionality(instances);
		}
		
		// label this single instance
		// clsLabel is 0.0 or 1.0
		double clsLabel = classifier.classifyInstance(instances.instance(0));
		instances.instance(0).setClassValue(clsLabel);
		return instances.instance(0).classAttribute().value((int) clsLabel);
	}
	
	
	public static String classifyActivationSingleUnlabeledInstance(MusicInstance mi, String modelObjPath, String attributeSelectionObjPath) throws Exception{
		Classifier classifier = (Classifier)weka.core.SerializationHelper.read(modelObjPath);
		Instances instances = new Instances(new StringReader(mi.toArffSingleInstanceFileActivationString()));
		// set class attribute
		instances.setClassIndex(instances.numAttributes() - 1);
		// performing attribute selection
		
		if(new File(attributeSelectionObjPath).exists()){
			AttributeSelection as = (AttributeSelection) weka.core.SerializationHelper.read(attributeSelectionObjPath);
			instances = as.reduceDimensionality(instances);
		}
		
		// label this single instance
		// clsLabel is 0.0 or 1.0
		double clsLabel = classifier.classifyInstance(instances.instance(0));
		instances.instance(0).setClassValue(clsLabel);
		return instances.instance(0).classAttribute().value((int) clsLabel);
	}
	/**
	 * Classifica um conjunto de instancias, assumindo q a classe eh o ultimo atributo,
	 * e salva um arquivo arff com os predictions.
	 * Caso as instancias ja estejam classificadas, a classe antiga eh sobrescrita.
	 * @param unlabeledArffPath caminho para o arquivo com as instancias
	 * @param modelObjPath
	 * @param attributeSelectionObjPath
	 * @param labeledArffPath caminho onde sera salvo o arquivo com as instancias classificadas
	 * @throws Exception
	 */
	
	
	public static void classifyUnlabeledInstances(String unlabeledArffPath, String modelObjPath, String attributeSelectionObjPath, String labeledArffPath) throws Exception{
		Classifier classifier = (Classifier) weka.core.SerializationHelper.read(modelObjPath);
		Instances instances = new Instances(new BufferedReader(new FileReader(unlabeledArffPath)));
		// set class attribute
		instances.setClassIndex(instances.numAttributes() - 1);
		// performing attribute selection
		AttributeSelection as = (AttributeSelection) weka.core.SerializationHelper.read(attributeSelectionObjPath);
		instances = as.reduceDimensionality(instances);
		// label instance
		// clsLabel is 0.0 or 1.0
		
		
		for (int i = 0, n = instances.numInstances(); i < n; i++) {
			double clsLabel = classifier.classifyInstance(instances.instance(i));
			instances.instance(i).setClassValue(clsLabel);
		}
		
		
		// save labeled data
		BufferedWriter writer = new BufferedWriter(new FileWriter(labeledArffPath));
		writer.write(instances.toString());
		writer.newLine();
		writer.flush();
		writer.close();
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