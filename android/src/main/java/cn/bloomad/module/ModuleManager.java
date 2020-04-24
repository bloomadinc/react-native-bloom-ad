package cn.bloomad.module;

import android.util.Log;

import java.util.HashMap;

public class ModuleManager {
    private static final String TAG = ModuleManager.class.getSimpleName();
    //创建 SingleObject 的一个对象
    private static ModuleManager instance = new ModuleManager();

    private HashMap<String, EventModule> map;

    //让构造函数为 private，这样该类就不会被实例化
    private ModuleManager(){
        this.map = new HashMap<String, EventModule>();
    }

    //获取唯一可用的对象
    public static ModuleManager getInstance(){
        return instance;
    }

    public void add(String name, EventModule eventModule){
        map.put(name, eventModule);
    }

    public void remove(String name){
        if(map.containsKey(name)){
            Log.i(TAG, "remove" + name);
            EventModule eventModule =  map.get(name);
            eventModule.destroy();
            map.remove(name);
        }
    }
}
