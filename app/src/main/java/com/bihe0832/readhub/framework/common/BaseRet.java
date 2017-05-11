package com.bihe0832.readhub.framework.common;

/**
 * @author code@bihe0832.com
 */

public class BaseRet {
    public static final int RET_SUCC = 0;
    public static final int RET_FAIL = 1;

    public int ret = RET_FAIL;
    public int flag = eFlag.Error;
    public String msg = "";


    public BaseRet() {
    }

    public BaseRet(BaseRet ret) {
        if (ret != null) {
            this.ret = ret.ret;
            this.flag = ret.flag;
            this.msg = ret.msg;
        }
    }

    public BaseRet(int ret, int flag, String msg) {
        this.ret = ret;
        this.flag = flag;
        this.msg = msg;
    }

    @Override
    public String toString() {
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append("ret : ").append(this.ret).append(";");
        tempBuilder.append("flag : ").append(this.flag).append(";");
        tempBuilder.append("msg : ").append(this.msg).append(";");
        return tempBuilder.toString();
    }
}

