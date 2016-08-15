package beacon.etri.dku.com.etribeaconreco.view;

/**
 * Created by user on 2016-02-17.
 */
public class UserFaveVo {
    private String contentName;
    private boolean isSelect;

    public UserFaveVo(String _contentName, boolean _isSelect) {
        this.contentName = _contentName;
        this.isSelect = _isSelect;
    }

    public String getContentName() {
        return contentName;
    }

    public boolean getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean _isSelect) {
        this.isSelect = _isSelect;
    }
}
