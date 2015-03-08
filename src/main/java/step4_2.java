

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class step4_2 {

    public static class Step4_RecommendMapper extends Mapper<LongWritable, Text, Text, Text> {

        @Override
        public void map(LongWritable key, Text values, Context context) throws IOException, InterruptedException {
            String[] tokens =Common.spliter.split(values.toString());
            Text k = new Text(tokens[0]);
            Text v = new Text(tokens[1]+","+tokens[2]);
            context.write(k, v);
        }
    }

    public static class Step4_RecommendReducer extends Reducer<Text, Text, Text, Text> {
        
        @Override
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            System.out.println(key.toString() + ":");
            Map<String, Double> map = new HashMap<String, Double>();// 结果
            
            for (Text line : values) {
                System.out.println(line.toString());
                String[] tokens = Common.spliter.split(line.toString());
                String itemID = tokens[0];
                Double score = Double.parseDouble(tokens[1]);
                
                 if (map.containsKey(itemID)) {
                     map.put(itemID, map.get(itemID) + score);// 矩阵乘法求和计算
                 } else {
                     map.put(itemID, score);
                 }
            }
            
            Iterator<String> iter = map.keySet().iterator();
            while (iter.hasNext()) {
                String itemID = iter.next();
                double score = map.get(itemID);
                Text v = new Text(itemID + "," + score);
                context.write(key, v);
            }
        }
    }

    public static void run(Map<String, String> path) throws Exception {
    	Configuration conf=new Configuration();
    	
        String input = path.get("Step6Input");
        String output = path.get("Step6Output");

        hdfsDAO hdfs = new hdfsDAO(Common.hdfs, conf);
        hdfs.rmr(output);

        Job job = new Job(conf);
        job.setJarByClass(step4_2.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(step4_2.Step4_RecommendMapper.class);
        job.setReducerClass(step4_2.Step4_RecommendReducer.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.setInputPaths(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        job.waitForCompletion(true);
    }

}
