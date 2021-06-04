package com.caiiiac.hosp.controller.api;

import com.caiiiac.hosp.exception.HospitalException;
import com.caiiiac.hosp.helper.HttpRequestHelper;
import com.caiiiac.hosp.model.hosp.Department;
import com.caiiiac.hosp.model.hosp.Hospital;
import com.caiiiac.hosp.model.hosp.Schedule;
import com.caiiiac.hosp.result.Result;
import com.caiiiac.hosp.result.ResultCodeEnum;
import com.caiiiac.hosp.service.DepartmentService;
import com.caiiiac.hosp.service.HospitalService;
import com.caiiiac.hosp.service.HospitalSetService;
import com.caiiiac.hosp.service.ScheduleService;
import com.caiiiac.hosp.utils.MD5;
import com.caiiiac.hosp.vo.hosp.DepartmentQueryVo;
import com.caiiiac.hosp.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
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

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;


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

        // 判断签名是否一致
        if (!checkSign(paramMap)) {
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
        // 参数信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 根据传递过来医院编码,查询数据库,查询签名
        String hoscode = (String) paramMap.get("hoscode");

        // 判断签名是否一致
        if (!checkSign(paramMap)) {
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }

        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }

    /**
     * 上传科室
     * @param request
     * @return
     */
    @PostMapping("saveDepa")
    public Result saveDepartment(HttpServletRequest request) {
        // 参数信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 判断签名是否一致
        if (!checkSign(paramMap)) {
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }

        departmentService.save(paramMap);
        return Result.ok();
    }

    /**
     * 查询科室
     * @param request
     * @return
     */
    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request) {
        // 参数信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        String hoscode = (String) paramMap.get("hoscode");
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : Integer.parseInt((String) paramMap.get("limit"));

        // 判断签名是否一致
        if (!checkSign(paramMap)) {
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);

        Page<Department> pageModel = departmentService.findPageDepartment(page, limit, departmentQueryVo);
        return Result.ok(pageModel);
    }

    /**
     * 删除科室
     * @param request
     * @return
     */
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        // 参数信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");

        // 判断签名是否一致
        if (!checkSign(paramMap)) {
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }
        departmentService.remove(hoscode, depcode);
        return Result.ok();
    }

    /**
     * 上传排班
     * @param request
     * @return
     */
    @PostMapping("saveSchedule")
    public Result saveSchedult(HttpServletRequest request) {
        // 参数信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 判断签名是否一致
        if (!checkSign(paramMap)) {
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }

        scheduleService.save(paramMap);
        return Result.ok();
    }

    /**
     * 查询排班
     * @param request
     * @return
     */
    @PostMapping("schedult/list")
    public Result findSchedult(HttpServletRequest request) {
        // 参数信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : Integer.parseInt((String) paramMap.get("limit"));

        // 判断签名是否一致
        if (!checkSign(paramMap)) {
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);

        Page<Schedule> pageModel = scheduleService.findPageSchedule(page, limit, scheduleQueryVo);
        return Result.ok(pageModel);
    }

    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        // 参数信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 判断签名是否一致
        if (!checkSign(paramMap)) {
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }

        String hoscode = (String) paramMap.get("hoscode");
        String hosSchedultId = (String) paramMap.get("hosSchedultId");

        scheduleService.remove(hoscode, hosSchedultId);
        return Result.ok();
    }

    // 检查签名
    private boolean checkSign(Map<String, Object> paramMap) {

        // 获取医院系统传递过来的签名,签名进行 MD5加密
        String hospSign = (String) paramMap.get("sign");

        // 根据传递过来医院编码,查询数据库,查询签名
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);

        // 把数据库查询签名进行 MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);
        // 判断签名是否一致
        return hospSign.equals(signKeyMD5);
    }
}
