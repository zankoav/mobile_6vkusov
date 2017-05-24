package by.netbix.alexandrzanko.mobile_6vkusov.Models;

/**
 * Created by alexandrzanko on 4/20/17.
 */

public class InfoRestaurant {

    private String _descriptionInfo;
    private String _addressInfo;
    private String _nameInfo;
    private String _unpInfo;
    private String _deliveryDescriptionInfo;
    private String _commercialRegisterInfo;

    public String get_descriptionInfo() {
        return _descriptionInfo;
    }

    public void set_descriptionInfo(String _descriptionInfo) {
        this._descriptionInfo = _descriptionInfo;
    }

    public String get_addressInfo() {
        return _addressInfo;
    }

    public void set_addressInfo(String _addressInfo) {
        this._addressInfo = _addressInfo;
    }

    public String get_nameInfo() {
        return _nameInfo;
    }

    public void set_nameInfo(String _nameInfo) {
        this._nameInfo = _nameInfo;
    }

    public String get_unpInfo() {
        return _unpInfo;
    }

    public void set_unpInfo(String _unpInfo) {
        this._unpInfo = _unpInfo;
    }

    public String get_deliveryDescriptionInfo() {
        return _deliveryDescriptionInfo;
    }

    public void set_deliveryDescriptionInfo(String _deliveryDescriptionInfo) {
        this._deliveryDescriptionInfo = _deliveryDescriptionInfo;
    }

    public String get_commercialRegisterInfo() {
        return _commercialRegisterInfo;
    }

    public void set_commercialRegisterInfo(String _commercialRegisterInfo) {
        this._commercialRegisterInfo = _commercialRegisterInfo;
    }

    public InfoRestaurant(String _descriptionInfo, String _addressInfo, String _nameInfo, String _unpInfo, String _deliveryDescriptionInfo, String _commercialRegisterInfo) {
        this._descriptionInfo = _descriptionInfo;
        this._addressInfo = _addressInfo;
        this._nameInfo = _nameInfo;
        this._unpInfo = _unpInfo;
        this._deliveryDescriptionInfo = _deliveryDescriptionInfo;
        this._commercialRegisterInfo = _commercialRegisterInfo;
    }

}
