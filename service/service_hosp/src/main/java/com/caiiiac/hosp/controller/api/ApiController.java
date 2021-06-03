package com.caiiiac.hosp.controller.api;

import com.caiiiac.hosp.exception.HospitalException;
import com.caiiiac.hosp.helper.HttpRequestHelper;
import com.caiiiac.hosp.model.hosp.Hospital;
import com.caiiiac.hosp.result.Result;
import com.caiiiac.hosp.result.ResultCodeEnum;
import com.caiiiac.hosp.service.HospitalService;
import com.caiiiac.hosp.service.HospitalSetService;
import com.caiiiac.hosp.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    /**
     * 上传医院接口
     * @param request
     * @return
     */
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request) {
        // 获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 获取医院系统传递过来的签名,签名进行 MD5加密
        String hospSign = (String) paramMap.get("sign");

        // 根据传递过来医院编码,查询数据库,查询签名
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);

        // 把数据库查询签名进行 MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);
        // 判断签名是否一致
        if (!hospSign.equals(signKeyMD5)) {
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }

        // 图片 base64传输过程中"+"转换为了" ",因此我们要转换回来
        String logoData = (String) paramMap.get("logoData");
        logoData = logoData.replaceAll(" ", "+");
        paramMap.put("logoData", logoData);

        // 调用 service 方法
        hospitalService.save(paramMap);
        return Result.ok();
    }

    /**
     * 查询医院
     * @return
     */
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request) {
        // 获取医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        // 医院编号
        String hoscode = (String) paramMap.get("hoscode");
        String hospSign = (String) paramMap.get("sign");

        String signKey = hospitalSetService.getSignKey(hoscode);

        String signKeyMD5 = MD5.encrypt(signKey);

        if (!hospSign.equals(signKeyMD5)) {
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }

        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }
}
