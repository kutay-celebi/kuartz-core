package com.kuartz.core.common.model;

import com.kuartz.core.common.domain.KzPageable;

import java.io.Serializable;

public class KuartzQueryModel implements Serializable {
    private static final long serialVersionUID = 6803637047885938752L;

    private KzPageable pageable;

    public KuartzQueryModel() {
        //    bos yapici
    }

    public KuartzQueryModel(KzPageable pageable) {
        this.pageable = pageable;
    }

    public KzPageable getPageable() {
        return pageable;
    }

    public void setPageable(KzPageable pageable) {
        this.pageable = pageable;
    }
}