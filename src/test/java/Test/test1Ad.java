package Test;

import common.java.security.codec;
import org.apache.kafka.common.protocol.types.Field;
import org.json.simple.JSONObject;

public class test1Ad {
    public static void main(String[] args) {
   /*     String  str ="{\n" +
                "    \"_id\" : ObjectId(\"592eaa451a4769cbf5ae25f3\"),\n" +
                "    \"adtype\" : NumberLong(2),\n" +
                "    \"imgURL\" : \"\\\\File\\\\upload\\\\2017-05-05\\\\zttp01.jpg\",\n" +
                "    \"uPlv\" : NumberLong(2000),\n" +
                "    \"adname\" : \"test\",\n" +
                "    \"dPlv\" : NumberLong(3000),\n" +
                "    \"rPlv\" : NumberLong(1000),\n" +
                "    \"linkURL\" : \"\",\n" +
                "    \"addesp\" : \"123\",\n" +
                "    \"width\" : \"123\",\n" +
                "    \"height\" : \"123\",\n" +
                "    \"wbid\" : \"59301f571a4769cbf5b0a0dd\",\n" +
                "    \"data\" : \"[{\\\"img\\\":\\\"http://www.jq22.com/demo/slide20160105/img/bleck.jpg\\\",\\\"text\\\":\\\"321\\\",\\\"url\\\":\\\"321\\\"},{\\\"img\\\":\\\"http://www.jq22.com/demo/slide20160105/img/white.jpg\\\",\\\"text\\\":\\\"123\\\",\\\"url\\\":\\\"123\\\"}]\"\n" +
                "}";*/
//        String str ="{\"_id\":\"59ad5dedc6c2040278e70a141\",\"adtype\":\"0\",\"d\":3000,\"data\":\"\",\"adsid\":\"0\",\"imgURL\":\"\",\"wbid\":\"59301f571a4769cbf5b0a0dd\",\"r\":1000,\"adheight\":\"100\",\"adname\":\"头部广告\",\"u\":2000,\"addesp\":\"123\",\"width\":\"123\",\"linkURL\":\"\",\"adwidth\":\"100\",\"height\":\"123\"}";
     // String str ="{\"d\":3000,\"data\":\"\",\"adsid\":\"0\",\"imgURL\":\"\",\"wbid\":\"59301f571a4769cbf5b0a0dd\",\"r\":1000,\"adheight\":\"100\",\"adname\":\"头部广告\",\"u\":2000,\"addesp\":\"123\",\"width\":\"123\",\"linkURL\":\"\",\"adwidth\":\"100\",\"height\":\"123\"}";
    	//String str ="{\"adtype\":\"0\",\"d\":3000,\"data\":\"\",\"adsid\":\"0\",\"imgURL\":\"\",\"wbid\":\"59301f571a4769cbf5b0a0dd\",\"r\":1000,\"adheight\":\"102\",\"adname\":\"头部广告1\",\"u\":2000,\"addesp\":\"123\",\"width\":\"123\",\"linkURL\":\"\",\"adwidth\":\"100\",\"height\":\"123\"}";
    	//String str ="{\"pptImage\":\"1231\",\"filetype\":1,\"fileoldname\":\"201502151551425358.jpg\",\"wbid\":\"\",\"filenewname\":\"15064131226479.jpg\",\"size\":\"18111\",\"filepath\":\"\\File/upload/2017-09-26\\15064131226479.jpg\",\"fileextname\":\"jpg\",\"ThumbnailImage\":\"\\File/upload/2017-09-26\\thumbnail\\15064131226479.jpg\",\"fatherid\":\"0\",\"isdelete\":0,\"time\":1506413122662,\"MD5\":\"72750a1d9732a3f26c24562535db0af2\"}";
    	//String str="{\"pptImage\":\"1231\",\"filetype\":1,\"fileoldname\":\"201502151551425358.jpg\",\"size\":\"18111\",\"filepath\":\"\\File/upload/2017-09-26\\15064131226479.jpg\",\"fileextname\":\"jpg\",\"ThumbnailImage\":\"\\File/upload/2017-09-26\\thumbnail\\15064131226479.jpg\",\"fatherid\":\"0\",\"isdelete\":0,\"time\":1506413122662,\"MD5\":\"72750a1d9732a3f26c24562535db0af2\"}";
    	String str ="{\"fatherid\":0,\"messageId\":\"00404cb71eb8444ab0c57d1b7a1df600\",\"messageDate\":1492506551,\"oid\":\"58d2595f0d42891d50e5d72f1\",\"floor\":4,\"messageContent\":\"UpdateMessage\",\"replynum\":1,\"isdelete\":\"1\",\"wbid\":\"594335af1a4769cbf5d04180\"}";
    	String encodeFastJSON = codec.encodeFastJSON(str);
        System.out.println(encodeFastJSON);
    }
}