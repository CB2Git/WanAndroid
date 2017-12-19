package com.wanandroid;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.litesuits.orm.log.OrmLog;
import com.wanandroid.model.db.UserManger;
import com.wanandroid.model.entity.WanAndroidUser;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.wanandroid", appContext.getPackageName());
    }

    @Test
    public void testUserManger() throws Exception {
        WanAndroidUser userInfo = UserManger.getUserInfo();
        assertNull(userInfo);

        List<Integer> mlit = new ArrayList<>();
        mlit.add(1);
        mlit.add(2);

        userInfo = new WanAndroidUser();
        userInfo.setId(123);
        userInfo.setUsername("ceshi ");
        userInfo.setPassword("测试");
        userInfo.setCollectIds(mlit);

        UserManger.updateUserInfo(userInfo);

        userInfo = UserManger.getUserInfo();
        OrmLog.i(userInfo);

        WanAndroidUser k = new WanAndroidUser();
        k.setId(123);
        k.setUsername("ceshi 233");
        k.setPassword("测试3333");
        k.setCollectIds(null);
        UserManger.updateUserInfo(k);
        userInfo = UserManger.getUserInfo();
        OrmLog.i(userInfo);


    }

}
