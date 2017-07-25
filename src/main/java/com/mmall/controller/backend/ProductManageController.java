package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ServerResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author Administrator
 * @create 2017-07-25-18:20
 */
@RequestMapping("/manage/product")
@Controller
public class ProductManageController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IFileService fileService;

    /**
     * 商品新增或修改
     * @param session
     * @param product
     * @return
     */
    @RequestMapping(value = "save.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if(userService.checkAdminRole(currentUser).success()){
            //操作产品的业务逻辑
            return productService.saveOrUpdateProduct(product);
        }else{
            return ServerResponse.createByErrorMessage("无权限");
        }
    }

    /**
     * 商品上下架
     * @param session
     * @param productId
     * @param productStatus
     * @return
     */
    @RequestMapping(value = "setSaleStatus.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId,Integer productStatus){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if(userService.checkAdminRole(currentUser).success()){
            //商品上下架
            return productService.setSaleStatus(productId,productStatus);
        }else{
            return ServerResponse.createByErrorMessage("无权限");
        }
    }

    /**
     * 获取产品详情
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "detail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, Integer productId){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if(userService.checkAdminRole(currentUser).success()){
            return productService.manageProductDetail(productId);
        }else{
            return ServerResponse.createByErrorMessage("无权限");
        }
    }

    /**
     * 商品详情分页列表
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "listDetail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse listDetail(HttpSession session,
                                     @RequestParam(value="pageNum",defaultValue = "1") int pageNum,
                                     @RequestParam(value="pageSize",defaultValue = "10") int pageSize){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if(userService.checkAdminRole(currentUser).success()){
            //分页
            return productService.getProductList(pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorMessage("无权限");
        }
    }

    /**
     * 根据产品id和产品名称搜索产品列表并分页
     * @param session
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "productSearch.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSearch(HttpSession session,
                                        String productName,
                                        Integer productId,
                                     @RequestParam(value="pageNum",defaultValue = "1") int pageNum,
                                     @RequestParam(value="pageSize",defaultValue = "10") int pageSize){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if(userService.checkAdminRole(currentUser).success()){
            //分页
            return productService.searchProduct(productName,productId,pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorMessage("无权限");
        }
    }

    /**
     * springmvc文件上传
     * @param session
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "upload.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
        }

        if(userService.checkAdminRole(user).success()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file,path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            Map fileMap = Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            return ServerResponse.createBySuccess(fileMap);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }

    }

    /**
     * 富文本文件上传
     * @param session
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "richtext_img_upload.do",method = RequestMethod.POST)
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            return resultMap;
        }

        //富文本编辑器SimDitor中返回值有自己的格式
        /*{
            "success":true/false,
            "msg":"error_message",
            "file_path":"[real file path]"
        }*/



        if(userService.checkAdminRole(user).success()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file,path);
            if (StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);

            //这个是Simditor约定好的
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;
        }else {
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }

    }
}

