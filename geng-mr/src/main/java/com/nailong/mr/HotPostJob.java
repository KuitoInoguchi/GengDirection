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
import java.util.Map;
import java.util.TreeMap;

/**
 * 实验四 处理层 · 作业一：热门帖子 Top-N。
 *
 * 输入：HDFS 上的行为日志，每行 TSV =  userId \t action \t postId \t ts
 * 思路：把每条行为按"行为类型权重"折算成热度分，按 postId 汇总求和，
 *       最后取热度最高的前 N 个帖子。
 * 输出：postId \t heatScore   （按热度从高到低）
 *
 * 用法：
 *   hadoop jar geng-mr-1.0.jar com.nailong.mr.HotPostJob <输入目录> <输出目录> [topN]
 */
public class HotPostJob extends Configured implements Tool {

    /** 行为权重，必须与后端 ActionType 的 weight 保持一致 */
    private static int weightOf(String action) {
        return switch (action == null ? "" : action.trim().toUpperCase()) {
            case "FAVORITE" -> 5;
            case "COMMENT" -> 3;
            case "CREATE_POST" -> 2;
            case "VIEW", "SEARCH", "TRACK" -> 1;
            default -> 0;
        };
    }

    // ========== Mapper：一行行为 -> (postId, 热度权重) ==========
    public static class HotPostMapper extends Mapper<LongWritable, Text, LongWritable, IntWritable> {
        private final LongWritable outKey = new LongWritable();
        private final IntWritable outVal = new IntWritable();

        @Override
        protected void map(LongWritable key, Text value, Context ctx)
                throws IOException, InterruptedException {
            String[] f = value.toString().split("\t");
            if (f.length < 3) {
                return;                       // 脏数据跳过
            }
            long postId;
            try {
                postId = Long.parseLong(f[2].trim());
            } catch (NumberFormatException e) {
                return;
            }
            if (postId <= 0) {
                return;                       // 无关联帖子的行为（如纯搜索）不计入帖子热度
            }
            int w = weightOf(f[1]);
            if (w == 0) {
                return;
            }
            outKey.set(postId);
            outVal.set(w);
            ctx.write(outKey, outVal);
        }
    }

    // ========== Reducer：按 postId 求和，并在 cleanup 里取 Top-N ==========
    public static class HotPostReducer extends Reducer<LongWritable, IntWritable, LongWritable, IntWritable> {
        // TreeMap 自动按热度升序排列，超过 topN 就丢弃最小的，保持只留前 N 个
        private final TreeMap<Integer, Long> top = new TreeMap<>();
        private int topN;

        @Override
        protected void setup(Context ctx) {
            topN = ctx.getConfiguration().getInt("hotpost.topN", 20);
        }

        @Override
        protected void reduce(LongWritable postId, Iterable<IntWritable> values, Context ctx) {
            int sum = 0;
            for (IntWritable v : values) {
                sum += v.get();
            }
            top.put(sum, postId.get());       // 注意：相同热度会覆盖，演示场景可接受
            if (top.size() > topN) {
                top.remove(top.firstKey());   // 移除当前最小热度
            }
        }

        @Override
        protected void cleanup(Context ctx) throws IOException, InterruptedException {
            // descendingMap 从高到低输出
            for (Map.Entry<Integer, Long> e : top.descendingMap().entrySet()) {
                ctx.write(new LongWritable(e.getValue()), new IntWritable(e.getKey()));
            }
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("用法: HotPostJob <输入目录> <输出目录> [topN]");
            return 2;
        }
        Configuration conf = getConf();
        conf.setInt("hotpost.topN", args.length >= 3 ? Integer.parseInt(args[2]) : 20);

        Job job = Job.getInstance(conf, "geng-hotpost-topN");
        job.setJarByClass(HotPostJob.class);
        job.setMapperClass(HotPostMapper.class);
        job.setReducerClass(HotPostReducer.class);
        // 取全局 Top-N，必须单 Reducer
        job.setNumReduceTasks(1);
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new Configuration(), new HotPostJob(), args));
    }
}
