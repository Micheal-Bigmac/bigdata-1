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

public class step3_2 {

	public static class step_3_2Vertormap extends
			Mapper<LongWritable, Text, Text, IntWritable> {
		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			String[] splits = Common.spliter.split(value.toString());
			context.write(new Text(splits[0]),
					new IntWritable(Integer.valueOf(splits[1])));
		}
	}

	public static void run(Map<String, String> path) throws Exception {
		String input = path.get("Step3Input2");
        String output = path.get("Step3Output2");
        
        Configuration configuration=new Configuration();
        hdfsDAO hdfs=new hdfsDAO(Common.hdfs,configuration);
        hdfs.rmr(output);
        
        Job job=new Job(configuration,Executor.class.getSimpleName());
        
        FileInputFormat.setInputPaths(job, input);
        job.setInputFormatClass(TextInputFormat.class);
        job.setMapperClass(step_3_2Vertormap.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        FileOutputFormat.setOutputPath(job,new Path(output));
        
        job.waitForCompletion(true);
	}

}
