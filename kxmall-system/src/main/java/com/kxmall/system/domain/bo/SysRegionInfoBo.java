package com.kxmall.system.domain.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Getter
@Setter
@ToString
public class SysRegionInfoBo {

    @NotBlank(message = "请填写可配送区域")
    private String regionName;

    @NotBlank(message = "请填写首件")
    private String first;

    @NotBlank(message = "请填写首件价格")
    private String price;

    @NotBlank(message = "请填写续件")
    @JsonProperty("_continue")
    private String _continue;

    @NotBlank(message = "请填写续件价格")
    @JsonProperty("continue_price")
    private String continue_price;

    private List<SysRegionBo> region;
}
