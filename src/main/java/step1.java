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


public class step1 {

	public static class step_1_map extends Mapper<LongWritable, Text, IntWritable, Text>{
		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, IntWritable, Text>.Context context)
				throws IOException, InterruptedException {
			String []split=Common.spliter.split(value.toString());
			int userid=Integer.parseInt(split[0]);
			String itemid=split[1];
			String val=split[2];
			context.write(new IntWritable(userid), new Text(itemid+":"+val));
		}
	}
	public static class step_1_reduce extends Reducer<IntWritable, Text, IntWritable, Text>{

		@Override
		protected void reduce(IntWritable arg0, Iterable<Text> arg1,
				Reducer<IntWritable, Text, IntWritable, Text>.Context arg2)
				throws IOException, InterruptedException {
			StringBuffer sb=new StringBuffer();
			for(Text temp : arg1){
				sb.append(","+temp.toString());
			}
			
			arg2.write(arg0, new Text(sb.toString().replaceFirst(",", "")));
		}
	}
	public static void run(Map<String, String> path) throws Exception{
		
		String input =path.get("Step1Input");
		String output=path.get("Step1Output");
		Configuration configuration=new Configuration();
		Job job =new Job(configuration, Executor.class.getSimpleName());
		
		hdfsDAO hdfs=new hdfsDAO(Common.hdfs, configuration);
		hdfs.rmr(input);
		hdfs.mkdir(input);
		hdfs.uploadFile(path.get("data"), input);
		FileInputFormat.setInputPaths(job, input);
		job.setInputFormatClass(TextInputFormat.class);
		job.setMapperClass(step_1_map.class);
		
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setReducerClass(step_1_reduce.class);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		FileOutputFormat.setOutputPath(job, new Path(output));
		job.waitForCompletion(true);
	}
}
