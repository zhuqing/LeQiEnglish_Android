package com.leqienglish.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.duoduo.entity.Content;
import cn.duoduo.entity.Entity;

import com.leqienglish.annotation.util.EntityAndJSON;

/**
 * catalogs=catalog_searchAll!searchAll.do
 * insertUser=user/user_addUser!addUser.do
 * checkEmail=user/user_checkEmail!checkEmail.do
 * getContentList=edit/content_getContentList!getContentList.do
 * getContentInfo=edit/content!getContentInfo.do
 * updateFile=updatefile/update.json
 * 
 * @author guona
 * 
 */
public class URLDataUtil {
	/**
	 * 服务地址
	 */
	public static final String HOST = "host";
	/**
	 * 获取分类URL的key
	 */
	public static final String GET_CATALOG = "catalogs";
	/**
	 * 插入用户的链接
	 */
	public static final String INSERT_USER = "insertUser";
	/**
	 * 检测邮件是否重复
	 */
	public static final String CHECK_IMAGE = "checkEmail";
	/**
	 * 获取ContentInfo
	 */
	public static final String CONTENT_INFO = "getContentInfo";
	/**
	 * 获取ContentInfo
	 */
	public static final String CONTENT_LIST = "getContentList";
	/**
	 * 获取ContentInfo
	 */
	public static final String CONTENT_LIST_TYPE = "getContentListByType";
	/**
	 * 获取ContentInfo
	 */
	public static final String CONTENT_LIST_PARENTID = "getContentListByParentId";
	
	/**
	 * 获取ContentInfo
	 */
	public static final String CONTENT_MORE_CONTENT = "getMoreContent";
	/**
	 * 获取ContentInfo
	 */
	public static final String CONTENT_NEW_CONTENT = "getNewContent";

	/**
	 * update文件
	 */
	public static final String UPDATE_APK = "updateApk";

	/**
	 * update文件
	 */
	public static final String CHECK_EMAIL = "checkEmail";
	/**
	 * 查找名言警句
	 */
	public static final String FIND_ALL_FAMOUS_WORDS = "famousWords";
	/**
	 * 获取单词
	 */
	public static final String DIC_FETCH_WORDS="fetchWords";

	/**
	 * 将json转换为List
	 * 
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public static List json2List(JSONObject jsonObject) throws Exception {
		
		List list = new ArrayList();
		if(jsonObject==null){
			return list;
		}
		JSONArray jsonArray = jsonObject.getJSONArray(AppType.LIST);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject json = jsonArray.getJSONObject(i);
			Object content = EntityAndJSON.json2Object(json);
			AppData.putContent((Entity) content);
			list.add(content);
		}
		return list;
	}

	public static List json2List(String json) throws Exception {
		if(json==null){
			return new ArrayList();
		}
		JSONObject jsonobject = JSONObject.fromObject(json);
		return json2List(jsonobject);
	}

	/**
	 * 
	 updateApk=/update.json #验证码图片 piccodeImage=servlet/ImageServlet
	 * ##=============ContentAction ##获取Content列表
	 * getContentList=edit/content_getContentList!getContentList.do
	 * ##获取Content信息
	 * getContentInfo=edit/content_getContentInfo!getContentInfo.do
	 * ##创建新的Content addWenZhang=edit/content_addWenZhang!addWenZhang.do
	 * 
	 * ##删除Content deleteContent=edit/content_deleteContent!deleteContent.do
	 * 
	 * ##高级权限删除Content
	 * allPrilegeDeleteContent=edit/content_allPrilegeDeleteContent
	 * !allPrilegeDeleteContent.do ##获取某一个分类的Content
	 * getContentListByCatalogId=edit
	 * /content_getContentListByCatalogId!getContentListByCatalogId.do
	 * ##根据类型的Content getContentListByType=edit/content_getContentListByType!
	 * getContentListByType.do ##根据parentId获取子的Content
	 * getContentListByParentId=edit
	 * /content_getContentListByParentId!getContentListByParentId.do
	 * 
	 * ##创建评论 addComment=edit/content_addComment!addComment.do ##通过Id获取评论内容
	 * getCommentById=edit/content_getCommentById!getCommentById.do ##获取评论
	 * getComment=edit/content_getComment!getComment.do ##更新评论
	 * updateRecomment=edit/content_updateRecomment!updateRecomment.do
	 * 
	 * ##上传课程文件 uploadFellowEnglishZip=edit/content_uploadFellowEnglishZip!
	 * uploadFellowEnglishZip.do
	 * 
	 * ##获取当前content的上一个和下一个content
	 * getContentNextUp=edit/content_getContentNextUp!getContentNextUp.do
	 * 
	 * 
	 * ##上传图片 updataImage=edit/content_updataImage!updataImage.do
	 * 
	 * 
	 * ##检测验证码 checkYanZhengMa=edit/content_checkYanZhengMa!checkYanZhengMa.do
	 * 
	 * ##获取名言 getTitleTip=edit/content_getTitleTip!getTitleTip.do
	 * 
	 * ##更新阅读数 updateReader=edit/content_updateReader!updateReader.do
	 * 
	 * ##end=============ContentAction
	 * 
	 * ##==============catalogAction #获取所有分类
	 * catalogs=catalog_searchAll!searchAll.do #创建分类
	 * insertCatalog=catalog_insert!insert.do #删除分类
	 * deleteCatalog=catalog_delete!delete.do ## end==============catalogAction
	 * ##===================userAction ##注册用户
	 * insertUser=user/user_addUser!addUser.do ##激活账号
	 * activityUser=user/user_activityUser!activityUser.do ##登陆
	 * login=user/user_login!login.do ##登出
	 * loginOut=user/user_loginOut!loginOut.do ##检测email是否已经注册
	 * checkEmail=user/user_checkEmail!checkEmail.do ##查找密码
	 * findPassword=user/user_findPassword!findPassword.do ##通过uuId 获取用户
	 * checkUserByUuid=user/user_checkUserByUuid!checkUserByUuid.do
	 * 
	 * ##修改用户密码 modifyPassword=user/user_modifyPassword!modifyPassword.do ##=
	 * end==================userAction
	 */

}
