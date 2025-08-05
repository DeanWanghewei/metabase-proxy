package org.wei.metabaseproxy.model;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年08月05日 15:02
 */
public class GaiaClientInfoModel {
    private String gaiaApiUrl;
    private String gaiaApiToken;

    public GaiaClientInfoModel(String gaiaApiUrl, String gaiaApiToken) {
        this.gaiaApiUrl = gaiaApiUrl;
        this.gaiaApiToken = gaiaApiToken;
    }

    public String getGaiaApiUrl() {
        return gaiaApiUrl;
    }
    public String getGaiaApiToken() {
        return gaiaApiToken;
    }

}
