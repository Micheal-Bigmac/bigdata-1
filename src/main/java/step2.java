import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class step2 {

	public static class step_2_map extends
			Mapper<LongWritable, Text, Text, IntWritable> {
		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			String[] split = Common.spliter.split(value.toString());
			for (int i = 1; i < split.length; i++) {
				String temp1=split[i].split(":")[0];
				for (int j = 1; j < split.length; j++) {
					String temp2=split[j].split(":")[0];
					context.write(new Text(temp1+":"+temp2), new IntWritable(1));
				}
			}
		}
	}
	public static class step_2_reduce extends Reducer<Text, IntWritable, Text, IntWritable>{
		@Override
		protected void reduce(Text arg0, Iterable<IntWritable> arg1,
				Reducer<Text, IntWritable, Text, IntWritable>.Context arg2)
				throws IOException, InterruptedException {
			int sum=0;
			for(IntWritable temp : arg1){
				sum+=temp.get();
			}
			arg2.write(arg0, new IntWritable(sum));
		}
	} 
	
	public static void run(Map<String, String> path) throws Exception{
		Configuration configuration = new Configuration();
		Job job=new Job(configuration, Executor.class.getSimpleName());
		
		String input=path.get("Step2Input");
		String output=path.get("Step2Output");
		
		hdfsDAO hdfs=new hdfsDAO(Common.hdfs, configuration);
		hdfs.rmr(output);
		
		FileInputFormat.setInputPaths(job, input);
		job.setInputFormatClass(TextInputFormat.class);
		job.setMapperClass(step_2_map.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		job.setReducerClass(step_2_reduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		FileOutputFormat.setOutputPath(job, new Path(output));
		
		job.waitForCompletion(true);
		
		
	}
}
