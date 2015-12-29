package net.loyin.netService;

/**
 * Created by Chao on 2015/11/10.
 */
public class ResolveDesc {
    /**
     * 消息头
     */
    private String Header;
    /**
     * 对应的VO
     */
    private String VoClass;

    public String getHeader() {
        return Header;
    }

    public void setHeader(String header) {
        Header = header;
    }

    public String getVoClass() {
        return VoClass;
    }

    public void setVoClass(String voClass) {
        VoClass = voClass;
    }
}
