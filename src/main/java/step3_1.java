import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class step3_1 {

	public static class step_3_UserVertormap extends Mapper<LongWritable, Text, IntWritable, Text>{
		
		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, IntWritable, Text>.Context context)
				throws IOException, InterruptedException {
			String[] splits=Common.spliter.split(value.toString());
			
			for(int i=1 ; i<splits.length; i++){
				String []temp=splits[i].split(":");
				int id=Integer.parseInt(temp[0]);
				String val=temp[1];
				context.write(new IntWritable(id), new Text(splits[0]+":"+val));
			}
		}
	}
	public static void run(Map<String, String> path)throws Exception{
		String input = path.get("Step3Input1");
		String output=path.get("Step3Output1");
		Configuration conf=new Configuration();
		hdfsDAO hdfs=new hdfsDAO(Common.hdfs, conf);
		hdfs.rmr(output);
		
		Job job = new Job(conf, Executor.class.getSimpleName());
		
		FileInputFormat.setInputPaths(job, input);
		job.setInputFormatClass(TextInputFormat.class);
		job.setMapperClass(step_3_UserVertormap.class);
		
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		FileOutputFormat.setOutputPath(job, new Path(output));
		job.waitForCompletion(true);
	}
}
