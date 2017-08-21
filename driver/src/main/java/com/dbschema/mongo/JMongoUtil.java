package com.dbschema.mongo;

import com.dbschema.mongo.parser.JsonLoaderCallback;
import com.dbschema.mongo.parser.JsonParseException;
import com.dbschema.mongo.parser.JsonParser;
import org.bson.Document;
import org.bson.types.*;

import java.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

public class JMongoUtil {


    public static Document parse( String text ){
        Thread.dumpStack();
        if ( text != null && text.trim().length() > 0 && !text.trim().startsWith("{")){
            throw new JsonParseException( "Json should start with '{'. Json text : " + text, 0 );
        }
        JsonLoaderCallback callback = new JsonLoaderCallback();
        JsonParser.parse( text, callback );
//        cnv( callback.map);
        return new Document(callback.map);
    }

//    public static ObjectId cnv(Object obj) {
//        if (obj instanceof Map) {
//            Map<String, Object>map = (Map<String, Object>) obj;
//            List<String> keys = map.keySet().stream().collect(Collectors.toList());
//
//            for (int i = 0; i < keys.size(); i++) {
//                String key = keys.get(i);
//                Object value = map.get(key);
//                if (key.equals("$oid")) {
//                    return (ObjectId) value;
//                }
//                if (value instanceof Map) {
//                    ObjectId ret = cnv(value);
//                    if(ret!=null){
//                        map.replace(key,ret);
//                    }
//                }
//                cnv(Object obj)
//            }
//        }
//        if (obj instanceof Collection) {
//            for (Object c:(List)obj) {
//                cnv(c);
//            }
//        }
//        return null;
//    }
    /*
    try {
            return Document.parse(text );
        } catch ( JsonParseException ex ){
            StringBuilder sb = new StringBuilder();
            String msg = ex.getLocalizedMessage();
            if ( msg != null ){
                if ( msg.endsWith(".")) msg = msg.substring(0, msg.length()-1);
                sb.append( msg );
            }
            sb.append( " in string " ).append( text);

            throw new JsonParseException( sb.toString() );
        }
     */



    public static Map doConversions(Map map){
        for (Object key : map.keySet()){
            Object value = map.get( key );
            if ( value instanceof Map ){
                doConversions((Map) value);
            }
            if ( value instanceof Map && canConvertMapToArray( (Map)value )){
                map.put( key, convertMapToArray((Map) value));
            }
        }
        return map;
    }


    private static boolean canConvertMapToArray( Map map ) {
        boolean isArray = true;
        for (int i = 0; i < map.size(); i++) {
            if (!map.containsKey("" + i)) isArray = false;
        }
        return isArray;
    }

    private static List convertMapToArray( Map map ) {
            ArrayList array = new ArrayList();
            for ( int i = 0; i < map.size(); i++ ){
                array.add(map.get("" + i));
            }
            return array;
    }



}
