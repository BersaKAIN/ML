package cs475;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class Classify {
	static public LinkedList<Option> options = new LinkedList<Option>();
	
	public static void main(String[] args) throws Exception {
		// Parse the command line.
		String[] manditory_args = { "mode"};
		createCommandLineOptions();
		CommandLineUtilities.initCommandLineParameters(args, Classify.options, manditory_args);
	
		String mode = CommandLineUtilities.getOptionValue("mode");
		String data = CommandLineUtilities.getOptionValue("data");
		String predictions_file = CommandLineUtilities.getOptionValue("predictions_file");
		String algorithm = CommandLineUtilities.getOptionValue("algorithm");
		String model_file = CommandLineUtilities.getOptionValue("model_file");
				
		if (mode.equalsIgnoreCase("train")) {
			if (data == null || algorithm == null || model_file == null) {
				System.out.println("Train requires the following arguments: data, algorithm, model_file");
				System.exit(0);
			}
			// Load the training data.
			DataReader data_reader = new DataReader(data, true);
			List<Instance> instances = data_reader.readData();
			data_reader.close();
			
			// Train the model.
			Predictor predictor = train(instances, algorithm);
			saveObject(predictor, model_file);		
			
		} else if (mode.equalsIgnoreCase("test")) {
			if (data == null || predictions_file == null || model_file == null) {
				System.out.println("Train requires the following arguments: data, predictions_file, model_file");
				System.exit(0);
			}
			
			// Load the test data.
			DataReader data_reader = new DataReader(data, true);
			List<Instance> instances = data_reader.readData();
			data_reader.close();
			
			// Load the model.
			Predictor predictor = (Predictor)loadObject(model_file);
			evaluateAndSavePredictions(predictor, instances, predictions_file);
		} else {
			System.out.println("Requires mode argument.");
		}
	}
	

	private static Predictor train(List<Instance> instances, String algorithm) throws Exception {
		Predictor p;
		if (algorithm.equals("even_odd")) {
			p = new EvenOddClassifier();
		}
		else if (algorithm.equals("majority")) {
			p = new MajorityClassifier();
		}
		else if (algorithm.equals("logistic_regression")) {
			p = new LogisticClassifier();
		}
		else if (algorithm.equals("margin_perceptron")) {
			p = new MarginPerceptron();
		}
		else if (algorithm.equals("perceptron_linear_kernel")) {
			p = new MarginKernelPerceptron(algorithm);
		}
		else if (algorithm.equals("perceptron_polynomial_kernel")) {
			p = new MarginKernelPerceptron(algorithm);
		}
		else if (algorithm.equals("mira")) {
			p = new MIRA();
		}
		else if (algorithm.equals("lambda_means")) {
			p = new LambdaMeansPredictor(instances);
		}
		else if (algorithm.equals("ska")) {
			p = new StochasticKMeansPredictor(instances);
		}
		else {
			throw new Exception("The algorithm is not supported");
		}
		p.train(instances);
		AccuracyEvaluator e = new AccuracyEvaluator();
		System.out.println("The accuracy is: "+ e.evaluate(instances, p));
		return p;
	}

	private static void evaluateAndSavePredictions(Predictor predictor,
			List<Instance> instances, String predictions_file) throws IOException {
		PredictionsWriter writer = new PredictionsWriter(predictions_file);
		// TODO Evaluate the model if labels are available.
		AccuracyEvaluator e = new AccuracyEvaluator();
		System.out.println("The accuracy is: "+ e.evaluate(instances, predictor));
		for (Instance instance : instances) {
			Label label = predictor.predict(instance);
			writer.writePrediction(label);
		}
		writer.close();		
	}

	public static void saveObject(Object object, String file_name) {
		try {
			ObjectOutputStream oos =
				new ObjectOutputStream(new BufferedOutputStream(
						new FileOutputStream(new File(file_name))));
			oos.writeObject(object);
			oos.close();
		}
		catch (IOException e) {
			System.err.println("Exception writing file " + file_name + ": " + e);
		}
	}

	/**
	 * Load a single object from a filename. 
	 * @param file_name
	 * @return
	 */
	public static Object loadObject(String file_name) {
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(file_name))));
			Object object = ois.readObject();
			ois.close();
			return object;
		} catch (IOException e) {
			System.err.println("Error loading: " + file_name);
		} catch (ClassNotFoundException e) {
			System.err.println("Error loading: " + file_name);
		}
		return null;
	}
	
	public static void registerOption(String option_name, String arg_name, boolean has_arg, String description) {
		OptionBuilder.withArgName(arg_name);
		OptionBuilder.hasArg(has_arg);
		OptionBuilder.withDescription(description);
		Option option = OptionBuilder.create(option_name);
		
		Classify.options.add(option);		
	}
	
	private static void createCommandLineOptions() {
		registerOption("data", "String", true, "The data to use.");
		registerOption("mode", "String", true, "Operating mode: train or test.");
		registerOption("predictions_file", "String", true, "The predictions file to create.");
		registerOption("algorithm", "String", true, "The name of the algorithm for training.");
		registerOption("model_file", "String", true, "The name of the model file to create/load.");
		registerOption("gd_eta", "int", true, "The step size parameter for GD.");
		registerOption("gd_iterations", "int", true, "The number of GD iterations.");
		registerOption("num_features_to_select", "int", true, "The number of features to select.");
		registerOption("online_learning_rate", "double", true, "The learning rate for perceptron.");
		registerOption("polynomial_kernel_exponent", "int", true, "The exponent of the polynomial kernel.");
		registerOption("online_training_iterations", "int", true, "The number of training iterations for online methods.");
		registerOption("cluster_lambda", "double", true, "The value of lambda in lambda-means.");
		registerOption("clustering_training_iterations", "int", true, "The number of clustering iterations.");
		registerOption("num_clusters", "int", true, "The number of clusters");


		
		// Other options will be added here.
	}
}
