package com.nailong.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * 实验四 处理层 · 作业二：用户行为类型分布统计。
 *
 * 这就是最经典的 WordCount 变体：把"单词"换成"行为类型"。
 * 输入：每行 TSV =  userId \t action \t postId \t ts
 * 输出：action \t count   （每种行为各发生了多少次）
 *
 * 用法：
 *   hadoop jar geng-mr-1.0.jar com.nailong.mr.ActionDistJob <输入目录> <输出目录>
 */
public class ActionDistJob extends Configured implements Tool {

    // ========== Mapper：取出 action 字段，输出 (action, 1) ==========
    public static class ActionMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        private static final IntWritable ONE = new IntWritable(1);
        private final Text outKey = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context ctx)
                throws IOException, InterruptedException {
            String[] f = value.toString().split("\t");
            if (f.length < 2) {
                return;
            }
            String action = f[1].trim().toUpperCase();
            if (action.isEmpty()) {
                return;
            }
            outKey.set(action);
            ctx.write(outKey, ONE);
        }
    }

    // ========== Reducer：对每种行为求和 ==========
    public static class ActionReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private final IntWritable result = new IntWritable();

        @Override
        protected void reduce(Text action, Iterable<IntWritable> values, Context ctx)
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable v : values) {
                sum += v.get();
            }
            result.set(sum);
            ctx.write(action, result);
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("用法: ActionDistJob <输入目录> <输出目录>");
            return 2;
        }
        Job job = Job.getInstance(getConf(), "geng-action-distribution");
        job.setJarByClass(ActionDistJob.class);
        job.setMapperClass(ActionMapper.class);
        job.setReducerClass(ActionReducer.class);
        // Combiner 与 Reducer 逻辑一致（求和满足结合律），可减少 shuffle 数据量
        job.setCombinerClass(ActionReducer.class);
        job.setNumReduceTasks(1);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new Configuration(), new ActionDistJob(), args));
    }
}
