package interfaceApplication;

import common.java.JGrapeSystem.rMsg;
import common.java.apps.appsProxy;
import common.java.authority.plvDef.UserMode;
import common.java.authority.plvDef.plvType;
import common.java.database.dbFilter;
import common.java.interfaceModel.GrapeDBDescriptionModel;
import common.java.interfaceModel.GrapePermissionsModel;
import common.java.interfaceModel.GrapeTreeDBModel;
import common.java.nlogger.nlogger;
import common.java.security.codec;
import common.java.session.session;
import common.java.string.StringHelper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Message {
    private GrapeTreeDBModel message;
    private GrapeDBDescriptionModel gDbSpecField;
    private GrapePermissionsModel   permissionsModel;
    private session se;
    private String currentWeb = null;
    private JSONObject userInfo = new JSONObject();
    private Integer userType = null;
    private String pkString;

    public Message() {
        message = new GrapeTreeDBModel();
        
        gDbSpecField = new GrapeDBDescriptionModel();
        gDbSpecField.importDescription(appsProxy.tableConfig("Message"));
        message.descriptionModel(gDbSpecField);
        
        permissionsModel = new GrapePermissionsModel();
        permissionsModel.importDescription(appsProxy.tableConfig("Message"));
        message.permissionsModel(permissionsModel);
        
        message.checkMode();//开启权限检查
        pkString = message.getPk();
        se = new session();
        userInfo=  se.getDatas();
        if (userInfo != null && userInfo.size() != 0) {
            currentWeb = userInfo.getString("currentWeb"); // 当前用户所属网站id
            userType = userInfo.getInt("userType");// 当前用户身份
        }
    }

    /**
     * 新增留言
     * 
     * @param msgInfo
     * @return
     */
    public String AddMessage(String msgInfo) {
        int type = 1;
        String fatherid = "0";
        String result = rMsg.netMSG(100, "新增留言失败");
        if (StringHelper.InvaildString(msgInfo)) {
            return rMsg.netMSG(1, "参数不合法");
        }
        System.out.println(codec.DecodeFastJSON(msgInfo));
        JSONObject object = JSONObject.toJSON(codec.DecodeFastJSON(msgInfo));
        if (object != null && object.size() > 0) {
            if (object.containsKey("fatherid")) {
                fatherid = object.get("fatherid").toString();
                if (!fatherid.equals("0")) {
                    type = 0;
                }
            }
            result = Add(object, fatherid, type);
        }
        return result;
    }

    /**
     * 新增操作
     * 
     * @param object
     * @param fatherid
     * @param type
     *            type为0.回复留言，回复次数+1，type为1，新增留言
     * @return
     */
    @SuppressWarnings("unchecked")
    private String Add(JSONObject object, String fatherid, int type) {
        int code = 0;
        String messageContent = "", oid = "";
        String result = rMsg.netMSG(100, "新增留言失败");
        JSONObject temp = new JSONObject();
        if (object != null && object.size() > 0) {
            switch (type) {
            case 0: // 回复留言，回复次数+1
                object.put("floor", 0);
                String replynum = String.valueOf(countReply(fatherid) + 1);
                temp.put("replynum", Integer.parseInt(replynum));
                code = update(object.get("fatherid").toString(), temp.toJSONString());
            case 1: // 新增留言
                if (code == 0) {
                    object.put("wbid", currentWeb);
                    if (object.containsKey("floor")) {
                        String temps = object.getString("floor");
                        if (!temps.equals("0")) {
                            long floor = message.ne("floor", 0).count() + 1;
                            object.put("floor", new Long(floor).longValue());
                    }
                    }
                    if (object.containsKey("messageContent")) {
                        messageContent = object.get("messageContent").toString();
                    }
                    if (!StringHelper.InvaildString(messageContent)) {
                        messageContent = codec.DecodeHtmlTag(messageContent);
                        messageContent = codec.decodebase64(messageContent);
                    }
                    object.escapeHtmlPut("messageContent", messageContent);
                    oid = (String) message.data(object).insertEx();
                    
                }
                break;
            }
        }
        return oid!=null ? rMsg.netMSG(0, "新增留言成功") : result;
    }

    /**
     * 修改留言
     * 
     * @param mid
     * @param msgInfo
     * @return
     */
    public String updateMessage(String mid, String msgInfo) {
        String result = rMsg.netMSG(100, "留言修改失败");
        int code = 99;
        if ((!StringHelper.InvaildString(mid)) && (!StringHelper.InvaildString(msgInfo))) {
            code = update(mid, msgInfo);
            result = code == 0 ? rMsg.netMSG(0, "留言修改成功") : result;
        }
        return result;
    }

    // 删除留言
    public String DeleteMessage(String mid) {
        return DeleteBatchMessage(mid);
    }

    // 批量删除留言
    public String DeleteBatchMessage(String mids) {
        String[] values = null;
        long code = 0;
        String result = rMsg.netMSG(100, "删除失败");
        if (!StringHelper.InvaildString(mids)) {
            values = mids.split(",");
        }
        if (values != null) {
            message.or();
            for (String mid : values) {
                message.eq(pkString, mid);
            }
            code = message.deleteAllEx();
            result = code > 0 ? rMsg.netMSG(0, "删除成功") : result;
        }
        return result;
    }

    /**
     * 隐藏或显示留言
     * 
     * @param //mid
     * @return
     */
    @SuppressWarnings("unchecked")
    public String MaskMessage(String mids, String isdelete) {
        String[] values = null;
        long code = 0;
        JSONObject object = new JSONObject();
        object.put("isdelete", Long.parseLong(isdelete));
        String result = rMsg.netMSG(100, "留言隐藏或显示失败");
        if (!StringHelper.InvaildString(mids)) {
            values = mids.split(",");
        }
        if (values != null) {
            message.or();
            for (String mid : values) {
                message.eq(pkString, mid);
            }
            code = message.data(object).updateAll();
            result = code > 0 ? rMsg.netMSG(0, "留言隐藏或显示成功") : result;
        }
        return result;
    }

    /**
     * 搜索留言
     * 
     * @param msgInfo
     * @return
     */
    public String SearchMessage(String msgInfo) {
        JSONArray array = null;
        JSONArray condArray = buildCond(msgInfo);
        System.out.println(condArray);
        if (condArray != null && condArray.size() > 0) {
            array = message.where(condArray).select();
        }
        return rMsg.netMSG(true, (array != null && array.size() > 0) ? dencode(array) : new JSONArray());
    }

    /**
     * 分页
     * 
     * @param idx
     * @param pageSize
     * @return
     */
    public String PageMessage(int idx, int pageSize) {
        return PageByMessage(idx, pageSize, null);
    }

    /**
     * 条件分页
     * 
     * @param idx
     * @param pageSize
     * @param msgInfo
     * @return
     */
    public String PageByMessage(int idx, int pageSize, String msgInfo) {
        long total = 0;
        JSONArray array = null;
//        if(msgInfo!=null){
        //if (!StringHelper.InvaildString(msgInfo)) {
            JSONArray condArray = buildCond(msgInfo);
            if (condArray == null || condArray.size() <= 0) {
                return rMsg.netPAGE(idx, pageSize, total, new JSONArray());
            } else {
                message.where(condArray);
            }
//        }
//        if(currentWeb!=null){
        if (!StringHelper.InvaildString(currentWeb)) {
        	//判断当前用户身份：系统管理员，网站管理员
        	message.eq("wbid", currentWeb);
        }
        array = message.dirty().page(idx, pageSize);
        total = message.count();
        return rMsg.netPAGE(idx, pageSize, total,(array != null && array.size() > 0) ? dencode(array) : new JSONArray());
    }

    /**
     * 修改操作
     * 
     * @param mid
     * @param msgInfo
     * @return
     */
    @SuppressWarnings("unused")
    private int update(String mid, String msgInfo) {
        int code = 99;
        if ((!StringHelper.InvaildString(mid)) && (!StringHelper.InvaildString(msgInfo))) {
            JSONObject object = JSONObject.toJSON(codec.DecodeFastJSON(msgInfo));
            if (object != null && object.size() > 0) {
            	System.out.println(message.eq(pkString, mid).data(object).updateEx());
            	code = message.eq(pkString, mid).data(object).updateEx()?0:99;
            }
        }
        return code;
    }

    /**
     * 获取回复次数
     * 
     * @param fid
     * @return
     */
    private long countReply(String fid) {
        long code = 0;
        try {
            code = message.eq("fatherid", fid).count();
        } catch (Exception e) {
            nlogger.logout(e);
            code = 0;
        }
        return code;
    }

    /**
     * 通过唯一标识符_id,查询留言信息
     * 
     * @param mid
     * @return
     */
    private JSONObject FindMsgByID(String mid) {
        JSONObject object = null;
        if (!StringHelper.InvaildString(mid)) {
            object = message.eq(pkString, mid).find();
        }
        return object != null ? dencode(object) : null;
    }
    
    /**
     * 整合参数，将JSONObject类型的参数封装成JSONArray类型
     * 
     * @param //object
     * @return
     */
    public JSONArray buildCond(String Info) {
        String key;
        Object value;
        JSONArray condArray = null;
        JSONObject object = JSONObject.toJSON(Info);
        dbFilter filter = new dbFilter();
        if (object != null && object.size() > 0) {
            for (Object object2 : object.keySet()) {
                key = object2.toString();
                value = object.get(key);
                filter.eq(key, value);
            }
            condArray = filter.build();
        } else {
            condArray = JSONArray.toJSONArray(Info);
        }
        return condArray;
    }

    /**
     * 留言内容解码
     * 
     * @param array
     * @return
     */
    @SuppressWarnings("unchecked")
    public JSONArray dencode(JSONArray array) {
        if (array.size() == 0) {
            return array;
        }
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = (JSONObject) array.get(i);
            array.set(i, dencode(object));
        }
        return array;
    }

    /**
     * 留言内容解码
     * 
     * @param //array
     * @return
     */
    @SuppressWarnings("unchecked")
    public JSONObject dencode(JSONObject object) {
        String content = "";
        if (object == null || object.size() <= 0) {
            return new JSONObject();
        }
        if (object.containsKey("messageContent")) {
            content = object.getString("messageContent");
            if (!StringHelper.InvaildString(content)) {
                object.put("messageContent", object.escapeHtmlGet("messageContent"));
            }
        }
        return object;
    }
}
