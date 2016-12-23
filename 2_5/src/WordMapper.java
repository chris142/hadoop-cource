import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.analysis.tokenattributes.*;


import java.util.regex.*;


import java.io.*;
import java.util.*;

public class WordMapper
extends Mapper<Object, Text, Text, Text>
{


    public void map(Object key, Text value, Context context)
	throws IOException, InterruptedException
	{

        String[] strings = value.toString().split("\\p{Punct}\\s|\\n");


        for (int i = 0; i<strings.length; ++i) {
            Reader reader = new StringReader(strings[i]);
            Analyzer analyzer = new StandardAnalyzer();

            TokenStream stream = analyzer.tokenStream(null, reader);

            stream.reset();


            String prev;
            String cur;
            if (stream.incrementToken()){
                prev = stream
                        .getAttribute(CharTermAttribute.class)
                        .toString();
                while (stream.incrementToken()) {
                    cur = stream
                            .getAttribute(CharTermAttribute.class)
                            .toString();
                    //?
                    context.write(new Text(prev), new Text(cur));
                    prev = cur;
                }
            }


            stream.end();
            stream.close();


        }

    }

}
