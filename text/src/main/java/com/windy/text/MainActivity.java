package com.windy.text;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Runnable{
    private PullToRefreshListView list;
    private ArrayList<String> data = new ArrayList<>();
    private ListAdapter adapter;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (PullToRefreshListView) findViewById(R.id.list);
        //设置list
        list.setMode(PullToRefreshBase.Mode.BOTH);//设置刷新模式，默认只有下拉
        //设置刷新提示
        ILoadingLayout loadingLayout = list.getLoadingLayoutProxy(
                false, //是否更改刷新头部信息
                true);//是否更改刷新尾部信息
        loadingLayout.setPullLabel("尾部开始刷新");
        loadingLayout.setRefreshingLabel("尾部正在刷新");
        loadingLayout.setReleaseLabel("尾部放开结束刷新");

        addData();
        adapter= new ListAdapter(data,this);
        list.setAdapter(adapter);//普通适配器

        //设置刷新监听，要使用OnRefreshListener2，
        list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override//下拉
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                data.clear();
                addData();
                adapter.notifyDataSetChanged();
                handler.post(MainActivity.this);//刷新结束，在UI线程中调用才有效果

            }

            @Override//上拉
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                addData();
                adapter.notifyDataSetChanged();
                handler.post(MainActivity.this);//刷新结束，在UI线程中调用才有效果
            }
        });
    }

    public void addData(){
        for (int i = 0; i < 24; i++) {
            data.add(i+"-----");
        }
    }

    @Override
    public void run() {
        list.onRefreshComplete();//刷新结束，在UI线程中调用才有效果
    }
}
