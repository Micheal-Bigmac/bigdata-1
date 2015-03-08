import java.util.HashMap;
import java.util.Map;


public class Executor {

	public static void main(String[] args) throws Exception {
		 Map<String, String> path = new HashMap<String, String>();
	        path.put("data", "logfile/small.csv");
	        path.put("Step1Input", Common.hdfs + "/2-9");
	        path.put("Step1Output", path.get("Step1Input") + "/step1");
	        path.put("Step2Input", path.get("Step1Output"));
	        path.put("Step2Output", path.get("Step1Input") + "/step2");
	        path.put("Step3Input1", path.get("Step1Output"));
	        path.put("Step3Output1", path.get("Step1Input") + "/step3_1");
	        path.put("Step3Input2", path.get("Step2Output"));
	        path.put("Step3Output2", path.get("Step1Input") + "/step3_2");
	        
	        path.put("Step4Input1", path.get("Step3Output1"));
	        path.put("Step4Input2", path.get("Step3Output2"));
	        path.put("Step4Output", path.get("Step1Input") + "/step4");
	        
	        path.put("Step5Input1", path.get("Step3Output1"));
	        path.put("Step5Input2", path.get("Step3Output2"));
	        path.put("Step5Output", path.get("Step1Input") + "/step5");
	        
	        path.put("Step6Input", path.get("Step5Output"));
	        path.put("Step6Output", path.get("Step1Input") + "/step6");
	        
	        step1.run(path);
	/*        step2.run(path);
	        step3_1.run(path);
	        step3_2.run(path);
	        step4_1.run(path);
	        step4_2.run(path);*/
	}

}
