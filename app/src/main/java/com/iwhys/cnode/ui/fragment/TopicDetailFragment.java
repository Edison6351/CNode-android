package com.iwhys.cnode.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iwhys.cnode.R;
import com.iwhys.cnode.util.ActivitySwitcher;
import com.iwhys.cnode.util.ShareUtils;
import com.iwhys.cnode.util.volley.UrlHelper;
import com.iwhys.cnode.widget.ScrollListenerWebView;

/**
 * 详情
 * Created by devil on 15/4/9.
 */
public class TopicDetailFragment extends BaseFragment {

    private ScrollListenerWebView webView;
    private String id, title, author, content, create_at;
    private int reply_count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null) {
            id = b.getString("id");
            title = b.getString("title");
            author = b.getString("author");
            content = b.getString("content");
            create_at = b.getString("create_at");
            reply_count = b.getInt("reply_count");
        }
        //设置显示菜单
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_detail, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.topic_detail);
        sActivity.setSupportActionBar(toolbar);
        webView = (ScrollListenerWebView) view.findViewById(R.id.web_view);
        TextView reply = (TextView) view.findViewById(R.id.reply);
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putString("id", id);
                ActivitySwitcher.pushFragment(sActivity, ReplyListFragment.class, arguments);
            }
        });
        reply.setText(String.valueOf(reply_count));
        loadData();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.share, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            ShareUtils.commonShare(sActivity, title + "\n" + UrlHelper.HOST + "/topic/" + id);
        }
        return super.onOptionsItemSelected(item);
    }

    //加载数据
    private void loadData() {
        String linkCss = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/topic_detail.css\" type=\"text/css\"/>";
        String t = "<h2 class=\"title\">" + title + "</h2>";
        String from = "<span class=\"from\">" + author + "&nbsp;&nbsp;" + create_at + "</span>";
        content = linkCss + t + from + content;
        content = content.replace("//dn-cnode", "https://dn-cnode");
        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
    }

}
