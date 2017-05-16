package com.bihe0832.readhub.framework.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.bihe0832.readhub.R;
import com.bihe0832.readhub.framework.activity.base.BaseActivity;
import com.bihe0832.readhub.framework.fragment.MainFragment;
import com.bihe0832.readhub.framework.fragment.WebClientFragment;
import com.bihe0832.readhub.framework.fragment.base.WebViewFragment;
import com.bihe0832.readhub.libware.file.Logger;
import com.bihe0832.readhub.libware.ui.ViewUtils;
import com.bihe0832.readhub.libware.util.TextUtils;
import com.bihe0832.readhub.module.about.AboutAuthorFragment;
import com.bihe0832.readhub.module.about.AboutReadhubFragment;
import com.bihe0832.readhub.module.update.ShakebaUpdate;
import com.bihe0832.readhub.network.ApiClient;

/**
 * @author code@bihe0832.com
 */

public class MainActivity extends BaseActivity {

    public static final String INTENT_EXTRA_KEY_ITEM_URL = "EXTRA_URL";
    public static final String INTENT_EXTRA_KEY_ITEM_TITLE = "EXTRA_TITLE";

    private DrawerLayout mDrawerLayout;//侧边菜单视图
    private ActionBarDrawerToggle mDrawerToggle;  //菜单开关
    private Toolbar mToolbar;
    private NavigationView mNavigationView;//侧边菜单项

    private FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;

    private boolean mIsHome = false;
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.d(intent);
        handleIntent(intent);
    }

    @Override
    protected int getLayoutResourceID() {
        return R.layout.com_bihe0832_shakeba_main_activity;
    }

    @Override
    protected void init() {
        mFragmentManager = getSupportFragmentManager();
        ApiClient.init("https://api.readhub.me");
    }


    private void handleIntent(Intent intent){
        if(null != intent && null != intent.getExtras()){
            if(null != intent.getExtras()){
                String url = "";
                if(intent.getExtras().containsKey(INTENT_EXTRA_KEY_ITEM_URL)){
                    url = intent.getExtras().getString(INTENT_EXTRA_KEY_ITEM_URL);
                }

                String name = "";
                if(intent.getExtras().containsKey(INTENT_EXTRA_KEY_ITEM_TITLE)){
                    name = intent.getExtras().getString(INTENT_EXTRA_KEY_ITEM_TITLE);
                }
                switchToWebClientFragment(name,url);

            }
        }
    }

    private void switchToWebClientFragment(String title, String url){
        if(TextUtils.ckIsEmpty(title)){
            title = getString(R.string.app_name);
        }
        if(!TextUtils.ckIsEmpty(url) && url.equalsIgnoreCase(getString(R.string.link_readhub_page))){
            mIsHome = true;
        }else{
            mIsHome = false;
        }
        WebClientFragment.setsURL(url);
        switchFragment(title,WebClientFragment.class);
    }
    //切换Fragment
    private void switchFragment(String titleName, Class<?> clazz) {
        if(TextUtils.ckIsEmpty(titleName)){
            mToolbar.setTitle(R.string.app_name);
        }else{
            mToolbar.setTitle(titleName);
        }
        Fragment to = ViewUtils.createFrgment(clazz);
        if (to.isAdded()) {
            mFragmentManager.beginTransaction().hide(mCurrentFragment).show(to).commit();
        } else {
            mFragmentManager.beginTransaction().hide(mCurrentFragment).add(R.id.frame_content, to).commit();
        }
        mCurrentFragment = to;
        if(to instanceof WebClientFragment){
            hideMenu(R.integer.menu_order_web);
        }else{
            showMenu(R.integer.menu_order_web);
        }
    }

    //初始化默认选中的Fragment
    private void initDefaultFragment() {
        mCurrentFragment = ViewUtils.createFrgment(MainFragment.class);
        mFragmentManager.beginTransaction().add(R.id.frame_content, mCurrentFragment).commit();
    }

    private void setNavigationViewItemClickListener() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_item_go_back:
                        switchFragment(getString(R.string.app_name),MainFragment.class);
                        break;
                    case R.id.navigation_item_home:
                        switchToWebClientFragment(getString(R.string.app_name),getString(R.string.link_readhub_page));
                        break;
                    case R.id.navigation_item_readhub:
                        switchFragment(getString(R.string.menu_key_app),AboutReadhubFragment.class);
                        break;
                    case R.id.navigation_item_me:
                        switchFragment(getString(R.string.menu_key_me),AboutAuthorFragment.class);
                        break;
                    case R.id.navigation_item_share:
                        shareToFriend(MainActivity.this);
                        break;
                    case R.id.navigation_item_update:
                        ShakebaUpdate.getInstance().checkUpdate(MainActivity.this,false);
                        break;
                    default:
                        break;
                }
                item.setChecked(true);
                mDrawerLayout.closeDrawer(Gravity.START);
                return false;
            }
        });
    }

    @Override
    protected void initView() {
        mToolbar = customFindViewById(R.id.toolbar);
        mDrawerLayout = customFindViewById(R.id.drawer_layout);
        mNavigationView = customFindViewById(R.id.navigation_view);

        mToolbar.setTitle(R.string.app_name);
        //这句一定要在下面几句之前调用，不然就会出现点击无反应
        setSupportActionBar(mToolbar);
        //ActionBarDrawerToggle配合Toolbar，实现Toolbar上菜单按钮开关效果。
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setNavigationViewItemClickListener();
        initDefaultFragment();
        ShakebaUpdate.getInstance().checkUpdate(this,true);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
            return;
        }

        if (mCurrentFragment instanceof WebViewFragment) {//如果当前的Fragment是WebViewFragment 则监听返回事件
            WebViewFragment webViewFragment = (WebViewFragment) mCurrentFragment;
            if (mIsHome && webViewFragment.canGoBack()) {
                webViewFragment.goBack();
                return;
            }else{
                switchFragment(getString(R.string.app_name),MainFragment.class);
            }
        }else{
            exitGame();
        }
    }

    private long lastBackKeyDownTick = 0;
    public static final long MAX_DOUBLE_BACK_DURATION = 1500;
    private void exitGame(){
        long currentTick = System.currentTimeMillis();
        if (currentTick - lastBackKeyDownTick > MAX_DOUBLE_BACK_DURATION) {
            Snackbar.make(mDrawerLayout, "再按一次退出", Snackbar.LENGTH_SHORT).show();
            lastBackKeyDownTick = currentTick;
        } else {
            finish();
        }
    }

    private void shareToFriend(final Activity activity){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
        sendIntent.setType("text/plain");
        activity.startActivity(Intent.createChooser(sendIntent, getString(R.string.share_title)));
    }

    private void showMenu(int id){
        MenuItem menuItem = mNavigationView.getMenu().getItem(getResources().getInteger(id));
        menuItem.setVisible(true);
        menuItem.setChecked(false);

    }

    private void hideMenu(int id){
        MenuItem menuItem = mNavigationView.getMenu().getItem(getResources().getInteger(id));
        menuItem.setVisible(false);
        menuItem.setChecked(false);
    }
}
