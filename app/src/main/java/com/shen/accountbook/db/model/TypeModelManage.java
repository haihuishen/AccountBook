package com.shen.accountbook.db.model;

import android.content.Context;
import android.database.Cursor;

import com.shen.accountbook.db.constant.Constant;
import com.shen.accountbook.db.table.TableEx;
import com.shen.accountbook.db.table.TypeEx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shen on 9/21 0021.
 */
public class TypeModelManage {

    private Cursor cursor_mainType;     // 主类型
    private Cursor cursor_type1;        // 次类型
    private Context mContext;
    private String typePath;            // "Type.db"的路径



    /** 存放所有解析(从数据库拿到)后的数据*/
    private List<MainTypeModel> mainTypeList;
    private TypeEx typeEx;            //

    /**
     *
     * @param context
     */
    public TypeModelManage(Context context){
        mContext = context;
        typePath = mContext.getFileStreamPath("Type.db").getPath();
        typeEx = new TypeEx(mContext);

        mainTypeList = new ArrayList<MainTypeModel>();
        getMainType();
    }

    public List<MainTypeModel> getMainTypeList() {
        return mainTypeList;
    }

    public void setMainTypeList(List<MainTypeModel> mainTypeList) {
        this.mainTypeList = mainTypeList;
    }

    private void getMainType(){
        cursor_mainType = typeEx.Query(Constant.TABLE_MAINTYPE, null, null ,null ,null, null, null);
        if(cursor_mainType.getCount() >= 1) {
            while(cursor_mainType.moveToNext()){
                MainTypeModel mainTypeModel = new MainTypeModel();
                mainTypeModel.setName(cursor_mainType.getString(1));
                mainTypeModel.setType1List(getType1List(cursor_mainType.getInt(0)));
                mainTypeList.add(mainTypeModel);
            }
            cursor_mainType.close();
            cursor_type1.close();
        }
    }

    /**
     * 获得"type1"类型 列表
     * @param mainTypeId    主类型的id(MainType 和 Type1 表中都有对应的)
     * @return
     */
    private List<Type1Model> getType1List(int mainTypeId){
        List<Type1Model> list = new ArrayList<Type1Model>();
        int i = 0;
        cursor_type1 = typeEx.Query(Constant.TABLE_TYPE1, null, "MainID=?" ,new String[]{String.valueOf(mainTypeId)} ,null, null, null);
//        cursor_type1 = typeEx.Query(Constant.TABLE_TYPE1, null, null ,null ,null, null, null);

        if(cursor_type1.getCount() >= 1) {
            while(cursor_type1.moveToNext()){
                Type1Model type1Model = new Type1Model();
                type1Model.setName(cursor_type1.getString(2));
                list.add(type1Model);
            }
        }
        return list;
    }

    /**
     * 拿到 maintype -- String[]
     * @return
     */
    public String[] mainType(){

        String[] mainType = new String[0];
        if(mainTypeList.size()!= 0 && mainTypeList != null){
            mainType = new String[mainTypeList.size()];
            for(int i=0; i<mainTypeList.size(); i++){
                mainType[i] = mainTypeList.get(i).getName();
            }
        }
        return mainType;
    }

    /**
     * 拿到 Type1 -- String[]
     * @return
     */
    public String[] type1(int mainType){

        String[] type1 = new String[0];
        List<Type1Model> list = new ArrayList<Type1Model>();
        list = mainTypeList.get(mainType).getType1List();
        if(list.size() != 0 && list != null){
            type1 = new String[list.size()];
            for(int i=0; i<list.size(); i++){
                type1[i] = list.get(i).getName();
            }
        }
        return type1;
    }

}
