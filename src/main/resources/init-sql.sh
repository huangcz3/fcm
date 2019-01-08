#!/usr/bin/env bash

schema=fcm
tbs=tbs_app_imcd
db2 -v "set current schema ${schema}"

#营销目的维表
db2 -v "drop table marketing_purpose"
db2 -v "create table marketing_purpose
(
   marketing_purpose_id SMALLINT,
   marketing_purpose_name VARCHAR(50),
   effective          SMALLINT
) in ${tbs}"
db2 -v "comment on table marketing_purpose is '营销目的维表'"
db2 -v "comment on column marketing_purpose.marketing_purpose_id is '营销目的id'"
db2 -v "comment on column marketing_purpose.marketing_purpose_name is '营销目的名称'"
db2 -v "comment on column marketing_purpose.effective is '是否有效：0无效，1有效'"
db2 -v "insert into marketing_purpose (marketing_purpose_id,marketing_purpose_name,effective) values (1,'新增客户类',1),(2,'存量保有类',1),(3,'价值提升类',1),(4,'离网预警类',1),(9,'其它类',1)"

#业务类型维表
db2 -v "drop table business_type"
db2 -v "create table business_type
(
   business_type_id   SMALLINT,
   business_type_name VARCHAR(50),
   effective          SMALLINT
) in ${tbs}"
db2 -v "comment on table business_type is '业务类型维表'"
db2 -v "comment on column business_type.business_type_id is '业务类型id'"
db2 -v "comment on column business_type.business_type_name is '业务类型名称'"
db2 -v "comment on column business_type.effective is '是否有效：0无效，1有效'"
db2 -v "insert into business_type (business_type_id,business_type_name,effective) values (1,'4G产品类',1),(2,'终端类',1),(3,'流量类',1),(4,'数字化服务类',1),(5,'基础服务类',1),(6,'PCC类',1),(7,'宽带类',1),(9,'其它类',1)"

#地市维表
db2 -v "drop table city"
db2 -v "create table city
(
   city_id            CHAR(2),
   city_name          VARCHAR(20)
) in ${tbs}"
db2 -v "comment on table city is '地市维表'"
db2 -v "comment on column city.city_id is '地市id'"
db2 -v "comment on column city.city_name is '地市名称'"
db2 -v "insert into city (city_id,city_name) values ('1','省公司'),('2','成都'),('3','绵阳'),('4','自贡'),('5','攀枝花'),('6','广元'),('7','达州'),('8','泸州'),('9','广安'),('10','巴中'),('11','遂宁'),('12','宜宾'),('13','内江'),('14','资阳'),('15','乐山'),('16','雅安'),('17','德阳'),('18','南充'),('19','眉山'),('20','阿坝'),('21','甘孜'),('22','凉山')"

#文件上传信息表
db2 -v "drop table upload_file"
db2 -v "create table upload_file
(
   file_name          VARCHAR(100),
   img_base64         CLOB(1048576),
   original_file_name VARCHAR(100),
   uploader_id        VARCHAR(100),
   uploader_name      VARCHAR(100),
   upload_time        TIMESTAMP,
   use_type           SMALLINT
) compress yes distribute by hash(file_name) in ${tbs}"
db2 -v "comment on table upload_file is '文件上传信息表'"
db2 -v "comment on column upload_file.file_name is '文件名'"
db2 -v "comment on column upload_file.img_base64 is 'base64编码图片'"
db2 -v "comment on column upload_file.original_file_name is '原始文件名'"
db2 -v "comment on column upload_file.uploader_id is '上传人id'"
db2 -v "comment on column upload_file.uploader_name is '上传人姓名'"
db2 -v "comment on column upload_file.upload_time is '上传时间'"
db2 -v "comment on column upload_file.use_type is '上传用途：1创建客户群，2创建自定义客户群，3创建剔除用客户群，4掌厅图片'"

#活动信息表
db2 -v "drop table activity_info"
db2 -v "create table activity_info
(
   activity_id        VARCHAR(50),
   activity_name      VARCHAR(100),
   creator_id         VARCHAR(100),
   creator_name       VARCHAR(100),
   create_time        TIMESTAMP,
   start_time         DATE,
   end_time           DATE,
   city_id            CHAR(2),
   city_name          VARCHAR(20),
   dept_id            VARCHAR(20),
   dept_name          VARCHAR(20),
   business_type_id   SMALLINT,
   business_type_name VARCHAR(50),
   marketing_purpose_id SMALLINT,
   marketing_purpose_name VARCHAR(50),
   scene_flag         SMALLINT,
   scene_id           SMALLINT,
   scene_name         VARCHAR(50),
   remove_employee    SMALLINT,
   remove_red_list    SMALLINT,
   remove_sensitive   SMALLINT,
   remove_cancel_10086 SMALLINT,
   remove_upload      SMALLINT,
   remove_customer_group_id VARCHAR(50),
   activity_state     SMALLINT,
   stopped            SMALLINT,
   deleted            SMALLINT,
   customer_update_cycle SMALLINT               default 1
) compress yes distribute by hash(activity_id) in ${tbs}"
db2 -v "comment on table activity_info is '活动信息表'"
db2 -v "comment on column activity_info.activity_id is '活动id'"
db2 -v "comment on column activity_info.activity_name is '活动名称'"
db2 -v "comment on column activity_info.creator_id is '创建人工号'"
db2 -v "comment on column activity_info.creator_name is '创建人姓名'"
db2 -v "comment on column activity_info.create_time is '创建时间'"
db2 -v "comment on column activity_info.start_time is '活动开始时间'"
db2 -v "comment on column activity_info.end_time is '活动结束时间'"
db2 -v "comment on column activity_info.city_id is '归属地市id'"
db2 -v "comment on column activity_info.city_name is '归属地市名'"
db2 -v "comment on column activity_info.dept_id is '部门id'"
db2 -v "comment on column activity_info.dept_name is '部门名称'"
db2 -v "comment on column activity_info.business_type_id is '业务类型id'"
db2 -v "comment on column activity_info.business_type_name is '业务类型名'"
db2 -v "comment on column activity_info.marketing_purpose_id is '营销目的id'"
db2 -v "comment on column activity_info.marketing_purpose_name is '营销目的名'"
db2 -v "comment on column activity_info.scene_flag is '是否场景活动'"
db2 -v "comment on column activity_info.scene_id is '场景id'"
db2 -v "comment on column activity_info.scene_name is '场景名'"
db2 -v "comment on column activity_info.remove_employee is '是否剔除内部号码：0否，1是'"
db2 -v "comment on column activity_info.remove_red_list is '是否剔除红名单：0否，1是'"
db2 -v "comment on column activity_info.remove_sensitive is '是否剔除敏感用户：0否，1是'"
db2 -v "comment on column activity_info.remove_cancel_10086 is '是否剔除取消10086流量提醒用户：0否，1是'"
db2 -v "comment on column activity_info.remove_upload is '是否剔除自定义上传用户：0否，1是'"
db2 -v "comment on column activity_info.remove_customer_group_id is '自定义剔除客户群id'"
db2 -v "comment on column activity_info.activity_state is '活动状态：0待生成目标用户，1目标用户生成成功，待内容审批，2目标用户生成失败，3目标用户生成成功但超过数量限制，4内容审批通过，5内容审批驳回，6渠道审批通过，7渠道审批驳回，8活动执行中，9结束'"
db2 -v "comment on column activity_info.stopped is '是否主动终止：0否，1是'"
db2 -v "comment on column activity_info.deleted is '是否已删除：0否，1是'"
db2 -v "comment on column activity_info.customer_update_cycle is '客户群更新周期：1一次性，2月周期，3日周期'"

#活动推荐产品表
db2 -v "drop table activity_recommend_product"
db2 -v "create table activity_recommend_product
(
   activity_id        VARCHAR(50),
   activity_name      VARCHAR(100),
   product_type       SMALLINT,
   product_id         VARCHAR(50),
   product_name       VARCHAR(100)
) compress yes distribute by hash(activity_id) in ${tbs}"
db2 -v "comment on table activity_recommend_product is '活动推荐产品表'"
db2 -v "comment on column activity_recommend_product.activity_id is '活动id'"
db2 -v "comment on column activity_recommend_product.activity_name is '活动名称'"
db2 -v "comment on column activity_recommend_product.product_type is '推荐类型：1资费，2营销活动，3宣传'"
db2 -v "comment on column activity_recommend_product.product_id is '推荐产品id'"
db2 -v "comment on column activity_recommend_product.product_name is '推荐产品名称'"

#客户群信息表
db2 -v "drop table customer_group_info"
db2 -v "create table customer_group_info
(
   customer_group_id  VARCHAR(50),
   customer_group_name VARCHAR(100),
   group_table_name   VARCHAR(100),
   creator_id         VARCHAR(100),
   creator_name       VARCHAR(100),
   create_time        TIMESTAMP,
   create_type        SMALLINT,
   amount             BIGINT,
   customize_flag     SMALLINT,
   effective          SMALLINT,
   remove_flag        SMALLINT,
   coc_group_id       VARCHAR(50),
   coc_group_name     VARCHAR(100),
   coc_table_name     VARCHAR(100),
   coc_group_cycle    SMALLINT,
   mpp_db_name        VARCHAR(20),
   mpp_schema         VARCHAR(20),
   mpp_table_name     VARCHAR(100),
   mpp_phone_column   VARCHAR(50),
   mpp_marketing_column VARCHAR(50),
   file_name          VARCHAR(100),
   original_file_name VARCHAR(100),
   file_line_count    BIGINT,
   migu_content_id    VARCHAR(50),
   migu_content_name  VARCHAR(100),
   migu_content_type  SMALLINT,
   migu_content_marketing VARCHAR(1000),
   migu_content_shorturl VARCHAR(200),
   migu_content_completed SMALLINT,
   migu_content_outputpath VARCHAR(1000),
   data_time          TIMESTAMP,
   data_state         SMALLINT
) compress yes distribute by hash(customer_group_id) in ${tbs}"
db2 -v "comment on table customer_group_info is '客户群信息表'"
db2 -v "comment on column customer_group_info.customer_group_id is '客户群id'"
db2 -v "comment on column customer_group_info.customer_group_name is '客户群名'"
db2 -v "comment on column customer_group_info.group_table_name is '客户群表名'"
db2 -v "comment on column customer_group_info.creator_id is '创建人id'"
db2 -v "comment on column customer_group_info.creator_name is '创建人姓名'"
db2 -v "comment on column customer_group_info.create_time is '创建时间'"
db2 -v "comment on column customer_group_info.create_type is '创建方式：1标签库，2数据集市，3文件上传，4数字内容'"
db2 -v "comment on column customer_group_info.amount is '客户数量'"
db2 -v "comment on column customer_group_info.customize_flag is '是否自定义：0否，1是'"
db2 -v "comment on column customer_group_info.effective is '是否有效：0无效，1有效'"
db2 -v "comment on column customer_group_info.remove_flag is '是否剔除用客户群：0否，1是'"
db2 -v "comment on column customer_group_info.coc_group_id is '标签库客户群id'"
db2 -v "comment on column customer_group_info.coc_group_name is '标签库客户群名'"
db2 -v "comment on column customer_group_info.coc_table_name is '标签库客户群表名'"
db2 -v "comment on column customer_group_info.coc_group_cycle is '标签库客户群周期：1一次性，2月周期，3日周期'"
db2 -v "comment on column customer_group_info.mpp_db_name is '集市数据库名'"
db2 -v "comment on column customer_group_info.mpp_schema is '集市schema'"
db2 -v "comment on column customer_group_info.mpp_table_name is '集市表名'"
db2 -v "comment on column customer_group_info.mpp_phone_column is '手机号字段名'"
db2 -v "comment on column customer_group_info.mpp_marketing_column is '个性化营销语字段名'"
db2 -v "comment on column customer_group_info.file_name is '文件名'"
db2 -v "comment on column customer_group_info.original_file_name is '原始文件名'"
db2 -v "comment on column customer_group_info.file_line_count is '原始文件记录数'"
db2 -v "comment on column customer_group_info.migu_content_id is '数字内容id'"
db2 -v "comment on column customer_group_info.migu_content_name is '数字内容名称'"
db2 -v "comment on column customer_group_info.migu_content_type is '数字内容类型：1内容推荐用户，2用户推荐内容-标签，3用户推荐内容-上传'"
db2 -v "comment on column customer_group_info.migu_content_marketing is '数字内容营销语'"
db2 -v "comment on column customer_group_info.migu_content_shorturl is '数字内容短链接地址'"
db2 -v "comment on column customer_group_info.migu_content_completed is '数字内容结果是否计算完成：0未完成，1已完成'"
db2 -v "comment on column customer_group_info.migu_content_outputpath is '数字内容结果输出地址'"
db2 -v "comment on column customer_group_info.data_time is '生成时间'"
db2 -v "comment on column customer_group_info.data_state is '数据状态：0未加载，1加载完成'"

#活动测试号码表
db2 -v "drop table activity_test_phone"
db2 -v "create table activity_test_phone
(
   activity_id        VARCHAR(50),
   test_phone_no      VARCHAR(20)
) compress yes distribute by hash(activity_id) in ${tbs}"
db2 -v "comment on table activity_test_phone is '活动测试号码表'"
db2 -v "comment on column activity_test_phone.activity_id is '活动id'"
db2 -v "comment on column activity_test_phone.test_phone_no is '测试号码'"

#场景维表
db2 -v "drop table scene"
db2 -v "create table scene
(
   scene_id           SMALLINT,
   scene_name         VARCHAR(50),
   effective          SMALLINT
) in ${tbs}"
db2 -v "comment on table scene is '场景维表'"
db2 -v "comment on column scene.scene_id is '场景id'"
db2 -v "comment on column scene.scene_name is '场景名'"
db2 -v "comment on column scene.effective is '是否有效：0无效，1有效'"
db2 -v "insert into scene (scene_id,scene_name,effective) values (1,'4G换卡',1),(2,'网厅缴费',1),(3,'业务订购',1),(4,'实时位置',1),(5,'上网行为',1),(6,'和包使用营销',1)"

#渠道类型维表
db2 -v "drop table channel_type"
db2 -v "create table channel_type
(
   channel_type_id    SMALLINT,
   channel_type_name  VARCHAR(50)
) in ${tbs}"
db2 -v "comment on table channel_type is '渠道类型维表'"
db2 -v "comment on column channel_type.channel_type_id is '渠道类型id'"
db2 -v "comment on column channel_type.channel_type_name is '渠道类型名称'"
db2 -v "insert into channel_type (channel_type_id,channel_type_name) values (1,'营业前台型'),(2,'短信型'),(3,'互联网渠道型')"

#渠道信息表
db2 -v "drop table channel"
db2 -v "create table channel
(
   channel_id         VARCHAR(10),
   channel_name       VARCHAR(50),
   channel_type_id    SMALLINT,
   channel_type_name  VARCHAR(50),
   need_content_approval SMALLINT,
   need_channel_approval SMALLINT,
   need_leader_approval SMALLINT,
   is_can_sms_approval SMALLINT,
   effective          SMALLINT,
   order_id           SMALLINT
) in ${tbs}"
db2 -v "comment on table channel is '渠道信息表'"
db2 -v "comment on column channel.channel_id is '渠道id'"
db2 -v "comment on column channel.channel_name is '渠道名称'"
db2 -v "comment on column channel.channel_type_id is '渠道类型id'"
db2 -v "comment on column channel.channel_type_name is '渠道类型名称'"
db2 -v "comment on column channel.need_content_approval is '是否需要内容审批：0不需要，1需要'"
db2 -v "comment on column channel.need_channel_approval is '是否需要渠道审批：0不需要，1需要'"
db2 -v "comment on column channel.need_leader_approval is '是否需要分管领导审批：0不需要，1需要'"
db2 -v "comment on column channel.is_can_sms_approval is '是否支持短信审批：0 不支持 1支持'"
db2 -v "comment on column channel.effective is '是否有效：0无效，1有效'"
db2 -v "comment on column channel.order_id is '排序字段：正序排列'"
db2 -v "insert into channel (channel_id,channel_name,channel_type_id,channel_type_name,need_content_approval,need_channel_approval,need_leader_approval,effective,order_id) values ('f01','前台弹窗/大掌柜',1,'营业前台型',1,0,0,1,1),('f02','客服系统',1,'营业前台型',1,1,0,1,2),('d01','掌上冲浪',2,'短信型',1,1,0,1,3),('d02','触点短信模式',2,'短信型',1,1,1,1,4),('d03','主动宣传模式',2,'短信型',1,1,1,1,5),('q03','掌厅',3,'互联网渠道型',1,1,0,1,9),('q05','和生活',3,'互联网渠道型',1,1,0,1,8),('q07','互联网基地渠道',3,'互联网渠道型',1,1,0,1,10),('q09','追尾短信模式',2,'短信型',1,1,1,1,6)"

#审批人配置表
db2 -v "drop table approver_info"
db2 -v "create table approver_info
(
   uuid               INT,
   approver_id        VARCHAR(100),
   approver_name      VARCHAR(100),
   approver_phone     VARCHAR(20),
   approver_level     SMALLINT,
   approval_group     VARCHAR(20),
   approval_role      VARCHAR(20),
   is_receive_sms	  SMALLINT
   effective          SMALLINT
) in ${tbs}"

db2 -v "comment on table approver_info is '审批人配置表'   "
db2 -v "comment on column approver_info.uuid is '自增长主键'   "
db2 -v "comment on column approver_info.approver_id is '审批人id'   "
db2 -v "comment on column approver_info.approver_name is '审批人姓名'   "
db2 -v "comment on column approver_info.approver_phone is '审批人手机'   "
db2 -v "comment on column approver_info.approver_level is '审批人级别'   "
db2 -v "comment on column approver_info.approval_group is '审批人所属审批组'   "
db2 -v "comment on column approver_info.approval_role is '审批人角色'   "
db2 -v "comment on column approver_info.is_receive_sms is '是否接收审批短息 0否 1是'   "
db2 -v "comment on column approver_info.effective is '是否有效：0无效，1有效'   "
#活动优先级信息表
db2 -v "drop table activity_priority"
db2 -v "create table activity_priority
(
   activity_id        VARCHAR(50),
   activity_name      VARCHAR(100),
   city_id            CHAR(2),
   city_name          VARCHAR(20),
   priority_time      TIMESTAMP
) compress yes distribute by hash(activity_id) in ${tbs}"
db2 -v "comment on table activity_priority is '活动优先级信息表'"
db2 -v "comment on column activity_priority.activity_id is '活动id'"
db2 -v "comment on column activity_priority.activity_name is '活动名称'"
db2 -v "comment on column activity_priority.city_id is '活动地市id'"
db2 -v "comment on column activity_priority.city_name is '地市名称'"
db2 -v "comment on column activity_priority.priority_time is '优先级设置时间（值越大优先级越高，新建默认创建时间）'"

#活动审批信息表
db2 -v "drop table activity_approval_info"
db2 -v "create table activity_approval_info
(
   activity_id        VARCHAR(50),
   activity_name      VARCHAR(100),
   channel_id         VARCHAR(10),
   channel_name       VARCHAR(100),
   approval_result    SMALLINT,
   approval_comments  VARCHAR(500),
   approver_id        VARCHAR(100),
   approver_name      VARCHAR(100),
   approver_phone     VARCHAR(20),
   approver_level     SMALLINT,
   approve_time       TIMESTAMP
) compress yes distribute by hash(activity_id) in ${tbs}"
db2 -v " comment on table activity_approval_info is '活动审批记录表'"
db2 -v " comment on column activity_approval_info.activity_id is '活动id'"
db2 -v " comment on column activity_approval_info.activity_name is '活动名称'"
db2 -v " comment on column activity_approval_info.channel_id is '渠道id'"
db2 -v " comment on column activity_approval_info.channel_name is '渠道名称'"
db2 -v " comment on column activity_approval_info.approval_result is '审批结果：0未审批，1审批通过，2审批驳回'"
db2 -v " comment on column activity_approval_info.approval_comments is '审批意见'"
db2 -v " comment on column activity_approval_info.approver_id is '审批人id'"
db2 -v " comment on column activity_approval_info.approver_name is '审批人姓名'"
db2 -v " comment on column activity_approval_info.approver_phone is '审批人手机号'"
db2 -v " comment on column activity_approval_info.approver_level is '审批人层级：1内容审批，2渠道审批，3分管领导审批'"
db2 -v " comment on column activity_approval_info.approve_time is '审批时间'"

#地市场景配置表
db2 -v "drop table city_scene_info"
db2 -v "create table city_scene_info
(
   city_id            CHAR(2),
   city_name          VARCHAR(20),
   scene_id           SMALLINT,
   scene_name         VARCHAR(50),
   effective          SMALLINT
) in ${tbs}"
db2 -v "comment on table city_scene_info is '地市场景配置表'"
db2 -v "comment on column city_scene_info.city_id is '地市id'"
db2 -v "comment on column city_scene_info.city_name is '地市名称'"
db2 -v "comment on column city_scene_info.scene_id is '场景id'"
db2 -v "comment on column city_scene_info.scene_name is '场景名称'"
db2 -v "comment on column city_scene_info.effective is '是否有效：0无效，1有效'"


#场景渠道配置表
db2 -v "drop table scene_channel_info"
db2 -v "create table scene_channel_info
(
   scene_id           SMALLINT,
   scene_name         VARCHAR(50),
   channel_id         VARCHAR(10),
   channel_name       VARCHAR(50),
   effective          SMALLINT
) in ${tbs}"
db2 -v "comment on table scene_channel_info is '场景渠道配置表'"
db2 -v "comment on column scene_channel_info.scene_id is '场景id'"
db2 -v "comment on column scene_channel_info.scene_name is '场景名称'"
db2 -v "comment on column scene_channel_info.channel_id is '渠道id'"
db2 -v "comment on column scene_channel_info.channel_name is '渠道名称'"
db2 -v "comment on column scene_channel_info.effective is '是否有效：0无效，1有效'"

#地市渠道配置表
db2 -v "drop table city_channel_info"
db2 -v "create table city_channel_info
(
   city_id            CHAR(2),
   city_name          VARCHAR(20),
   channel_id         VARCHAR(10),
   channel_name       VARCHAR(50),
   effective          SMALLINT
) in ${tbs}"
db2 -v "comment on table city_channel_info is '地市渠道配置表'"
db2 -v "comment on column city_channel_info.city_id is '地市id'"
db2 -v "comment on column city_channel_info.city_name is '地市名称'"
db2 -v "comment on column city_channel_info.channel_id is '渠道id'"
db2 -v "comment on column city_channel_info.channel_name is '渠道名称'"
db2 -v "comment on column city_channel_info.effective is '是否有效'"
db2 -v "insert into city_channel_info select a.city_id,a.city_name,b.channel_id,b.channel_name,1 effective from city a left join channel b on 1=1"

#活动客户群信息表
db2 -v "drop table activity_customer_group_info"
db2 -v "create table activity_customer_group_info
(
   activity_id        VARCHAR(50),
   activity_name      VARCHAR(100),
   customer_group_id  VARCHAR(50),
   customer_group_name VARCHAR(100),
   create_type        SMALLINT
) compress yes distribute by hash(activity_id) in ${tbs}"
db2 -v "comment on table activity_customer_group_info is '活动客户群信息表'"
db2 -v "comment on column activity_customer_group_info.activity_id is '活动id'"
db2 -v "comment on column activity_customer_group_info.activity_name is '活动名称'"
db2 -v "comment on column activity_customer_group_info.customer_group_id is '客户群id'"
db2 -v "comment on column activity_customer_group_info.customer_group_name is '客户群名称'"
db2 -v "comment on column activity_customer_group_info.create_type is '创建方式：1标签库，2数据集市，3文件上传，4数字内容'"

#活动目标用户生成信息表
db2 -v "drop table activity_customer_remove_info"
db2 -v "create table activity_customer_remove_info
(
   activity_id        VARCHAR(50),
   activity_name      VARCHAR(100),
   original_amount    BIGINT,
   remove_employee    SMALLINT,
   remove_employee_amount BIGINT,
   remove_red_list    SMALLINT,
   remove_red_list_amount BIGINT,
   remove_sensitive   SMALLINT,
   remove_sensitive_amount BIGINT,
   remove_cancel_10086 SMALLINT,
   remove_cancel_10086_amount BIGINT,
   remove_upload      SMALLINT,
   remove_upload_amount BIGINT,
   customize_flag     SMALLINT,
   final_group_table_name VARCHAR(100),
   final_amount       BIGINT,
   state              SMALLINT,
   cycle_update_completed SMALLINT               default 1,
   exception_info     VARCHAR(4000)
) compress yes distribute by hash(activity_id) in ${tbs}"
db2 -v "comment on table activity_customer_remove_info is '活动目标用户生成信息表'"
db2 -v "comment on column activity_customer_remove_info.activity_id is '活动id'"
db2 -v "comment on column activity_customer_remove_info.activity_name is '活动名称'"
db2 -v "comment on column activity_customer_remove_info.original_amount is '原始客户数'"
db2 -v "comment on column activity_customer_remove_info.remove_employee is '是否剔除内部员工：0否，1是'"
db2 -v "comment on column activity_customer_remove_info.remove_employee_amount is '剔除内部员工数'"
db2 -v "comment on column activity_customer_remove_info.remove_red_list is '是否剔除红名单：0否，1是'"
db2 -v "comment on column activity_customer_remove_info.remove_red_list_amount is '剔除红名单用户数'"
db2 -v "comment on column activity_customer_remove_info.remove_sensitive is '是否剔除敏感用户：0否，1是'"
db2 -v "comment on column activity_customer_remove_info.remove_sensitive_amount is '剔除敏感用户数'"
db2 -v "comment on column activity_customer_remove_info.remove_cancel_10086 is '是否剔除取消10086流量提醒用户：0否，1是'"
db2 -v "comment on column activity_customer_remove_info.remove_cancel_10086_amount is '剔除取消10086流量提醒用户数'"
db2 -v "comment on column activity_customer_remove_info.remove_upload is '是否剔除自定义用户：0否，1是'"
db2 -v "comment on column activity_customer_remove_info.remove_upload_amount is '剔除自定义用户数'"
db2 -v "comment on column activity_customer_remove_info.customize_flag is '是否自定义营销语：0否，1是'"
db2 -v "comment on column activity_customer_remove_info.final_group_table_name is '最终客户群表名'"
db2 -v "comment on column activity_customer_remove_info.final_amount is '最终用户数'"
db2 -v "comment on column activity_customer_remove_info.state is '目标用户生成状态：0未生成，1生成成功，2生成失败'"
db2 -v "comment on column activity_customer_remove_info.cycle_update_completed is '周期更新完成标示：0未完成，1已完成'"
db2 -v "comment on column activity_customer_remove_info.exception_info is '错误信息'"

#渠道规则维表
db2 -v "drop table channel_rule"
db2 -v "create table channel_rule
(
   rule_id            CHAR(4),
   rule_name          VARCHAR(100)
) in ${tbs}"
db2 -v "comment on table channel_rule is '渠道规则维表'"
db2 -v "comment on column channel_rule.rule_id is '渠道规则id'"
db2 -v "comment on column channel_rule.rule_name is '渠道规则名称'"
db2 -v "insert into channel_rule(rule_id,rule_name) values ('R001','营销用语/短信内容'),('R002','运营位id'),('R003','运营位类型'),('R004','运营位目录'),('R005','图片'),('R006','图片链接'),('R007','跳转链接'),('R008','提醒类型'),('R009','渠道小类'),('R010','链接码表'),('R011','标题')"

#活动渠道信息表
db2 -v "drop table activity_channel_info"
db2 -v "create table activity_channel_info
(
   activity_id        VARCHAR(50),
   activity_name      VARCHAR(100),
   channel_id         VARCHAR(10),
   channel_name       VARCHAR(50),
   rule_id            CHAR(4),
   rule_value         VARCHAR(1000),
   rule_extended_value VARCHAR(1000)
) compress yes distribute by hash(activity_id) in ${tbs}"
db2 -v "comment on table activity_channel_info is '活动渠道信息表'"
db2 -v "comment on column activity_channel_info.activity_id is '活动id'"
db2 -v "comment on column activity_channel_info.activity_name is '活动名称'"
db2 -v "comment on column activity_channel_info.channel_id is '渠道id'"
db2 -v "comment on column activity_channel_info.channel_name is '渠道名称'"
db2 -v "comment on column activity_channel_info.rule_id is '渠道规则id'"
db2 -v "comment on column activity_channel_info.rule_value is '渠道规则值'"
db2 -v "comment on column activity_channel_info.rule_extended_value is '渠道规则扩展值'"

#任务表
db2 -v "drop table task"
db2 -v "create table task
(
   task_id            VARCHAR(50),
   task_name          VARCHAR(100),
   creator_id         VARCHAR(100),
   creator_name       VARCHAR(100),
   create_time        TIMESTAMP,
   start_time         DATE,
   end_time           DATE,
   city_id            CHAR(2),
   city_name          VARCHAR(20),
   business_type_id   SMALLINT,
   business_type_name VARCHAR(50),
   marketing_purpose_id SMALLINT,
   marketing_purpose_name VARCHAR(50),
   effective          SMALLINT
) compress yes distribute by hash(task_id) in ${tbs}"
db2 -v "comment on table task is '任务表'"
db2 -v "comment on column task.task_id is '任务id'"
db2 -v "comment on column task.task_name is '任务名称'"
db2 -v "comment on column task.creator_id is '创建人id'"
db2 -v "comment on column task.creator_name is '创建人姓名'"
db2 -v "comment on column task.create_time is '创建时间'"
db2 -v "comment on column task.start_time is '开始时间'"
db2 -v "comment on column task.end_time is '结束时间'"
db2 -v "comment on column task.city_id is '地市id'"
db2 -v "comment on column task.city_name is '地市名称'"
db2 -v "comment on column task.business_type_id is '业务类型id'"
db2 -v "comment on column task.business_type_name is '业务类型名'"
db2 -v "comment on column task.marketing_purpose_id is '营销目的id'"
db2 -v "comment on column task.marketing_purpose_name is '营销目的名'"
db2 -v "comment on column task.effective is '是否有效：0无效，1有效'"

#任务活动表
db2 -v "drop table task_activity"
db2 -v "create table task_activity
(
   task_id            VARCHAR(50),
   task_name          VARCHAR(100),
   activity_id        VARCHAR(50),
   activity_name      VARCHAR(100)
) compress yes distribute by hash(task_id) in ${tbs}"
db2 -v "comment on table task_activity is '任务活动表'"
db2 -v "comment on column task_activity.task_id is '任务id'"
db2 -v "comment on column task_activity.task_name is '任务名称'"
db2 -v "comment on column task_activity.activity_id is '活动id'"
db2 -v "comment on column task_activity.activity_name is '活动名称'"

#场景规则配置表
db2 -v "drop table scene_rule"
db2 -v "create table scene_rule
(
   scene_rule_id      CHAR(5),
   scene_rule_name    VARCHAR(100)
) in ${tbs}"
db2 -v "comment on table scene_rule is '场景规则配置表'"
db2 -v "comment on column scene_rule.scene_rule_id is '场景规则id'"
db2 -v "comment on column scene_rule.scene_rule_name is '场景规则名称'"

#活动场景信息表
db2 -v "drop table activity_scene_info"
db2 -v "create table activity_scene_info
(
   activity_id        VARCHAR(50),
   activity_name      VARCHAR(100),
   scene_id           SMALLINT,
   scene_name         VARCHAR(50),
   scene_rule_id      CHAR(5),
   scene_rule_value   VARCHAR(1000),
   scene_rule_extended_value VARCHAR(1000)
) compress yes distribute by hash(activity_id) in ${tbs}"
db2 -v "comment on table activity_scene_info is '活动场景信息表'"
db2 -v "comment on column activity_scene_info.activity_id is '活动id'"
db2 -v "comment on column activity_scene_info.activity_name is '活动名称'"
db2 -v "comment on column activity_scene_info.scene_id is '场景id'"
db2 -v "comment on column activity_scene_info.scene_name is '场景名称'"
db2 -v "comment on column activity_scene_info.scene_rule_id is '场景规则id'"
db2 -v "comment on column activity_scene_info.scene_rule_value is '场景规则值'"
db2 -v "comment on column activity_scene_info.scene_rule_extended_value is '场景规则扩展值'"

#上网行为app类型配置表
db2 -v "drop table app_type_define"
db2 -v "create table app_type_define
(
   app_type_id           INTEGER,
   app_type_name      VARCHAR(50),
   app_type_desc      VARCHAR(50),
   effective          INTEGER
) in ${tbs}"
db2 -v "comment on column app_type_define.app_type_id is 'app类型'"
db2 -v "comment on column app_type_define.app_type_name is 'app类型名称'"
db2 -v "comment on column app_type_define.app_type_desc is 'app类型描述'"
db2 -v "comment on column app_type_define.effective is '0 无效 1 有效'"

#短信审批记录表
db2 -v "drop table activity_approval_sms_info"
db2 -v "create table activity_approval_sms_info
(
    sms_code			 integer	not null  generated by default
    as identity (start with 10000, increment by 1, cache 20, minvalue 10000, maxvalue 2147483647, no cycle, no order),
	activity_id          varchar(50),
	activity_name        varchar(200),
	channel_id           varchar(20),
	channel_name         varchar(50),
	activity_content     varchar(1000),
	approver_id          varchar(100),
	approver_name        varchar(50),
	approver_level       smallint,
	approver_phone       varchar(50),
	sms_approval_result  smallint,
	sms_send_time        timestamp,
	sms_send_state       smallint
) compress yes distribute by hash(sms_code) in ${tbs}"
db2 -v "comment on column activity_approval_sms_info.sms_code is '短信发送码'"
db2 -v "comment on column activity_approval_sms_info.activity_id is '活动id'"
db2 -v "comment on column activity_approval_sms_info.activity_name is '活动名称'"
db2 -v "comment on column activity_approval_sms_info.channel_id is '渠道id'"
db2 -v "comment on column activity_approval_sms_info.channel_name is '渠道名称'"
db2 -v "comment on column activity_approval_sms_info.activity_content is '活动内容'"
db2 -v "comment on column activity_approval_sms_info.approver_name is '审批人'"
db2 -v "comment on column activity_approval_sms_info.approver_level is '审批人层级'"
db2 -v "comment on column activity_approval_sms_info.approver_phone is '审批人电话'"
db2 -v "comment on column activity_approval_sms_info.approval_type is '1线上审批 2短信审批'"
db2 -v "comment on column activity_approval_sms_info.sms_approval_result is '0未审批 1审批通过 2审批驳回'"
db2 -v "comment on column activity_approval_sms_info.sms_send_time is '短信发送时间'"
db2 -v "comment on column activity_approval_sms_info.sms_send_state is '短信发送状态'"

#短信发送模板表
db2 -v "drop table sms_template"
db2 -v "create table sms_template
(
   template_id        SMALLINT,
   template_type      SMALLINT,
   channel_id         VARCHAR(20),
   approval_level     SMALLINT,
   template_content   VARCHAR(2000),
   effective          SMALLINT
) in ${tbs}"
db2 -v "comment on column sms_template.template_id is '模板id'"
db2 -v "comment on column sms_template.template_type is '模板类型：1审批模板 2审批通过模板 3审批驳回模板'"
db2 -v "comment on column sms_template.channel_id is '渠道id'"
db2 -v "comment on column sms_template.approval_level is '审批层级'"
db2 -v "comment on column sms_template.template_content is '模板内容'"
db2 -v "comment on column sms_template.effective is '是否有效 0无效  1有效'"

# 用户权限信息表
db2 -v "drop table user_role_info"
db2 -v "create table user_role_info
(
   user_id            VARCHAR(50),
   user_role          VARCHAR(20)
) in ${tbs}"
db2 -v "comment on column user_role_info.user_id is '用户id'"
db2 -v "comment on column user_role_info.user_role is '用户角色'"

#活动营销用户生成状态表
db2 -v "drop table activity_customers_build_state"
db2 -v "create table activity_customers_build_state
(
   activity_id        VARCHAR(50),
   activity_name      VARCHAR(100),
   source_is_complete SMALLINT,
   user_file_dir      VARCHAR(200),
   is_complete        SMALLINT
) compress yes distribute by hash(activity_id) in ${tbs}"
db2 -v "comment on column activity_customers_build_state.activity_id is '活动id'"
db2 -v "comment on column activity_customers_build_state.activity_name is '活动名称'"
db2 -v "comment on column activity_customers_build_state.source_is_complete is '源目标用户是否 0未生成 1已生成'"
db2 -v "comment on column activity_customers_build_state.user_file_dir is '目标用户文件下载路径'"
db2 -v "comment on column activity_customers_build_state.is_complete is '是否生成 0未生成 1已生成'"

#全网活动关联信息
db2 -v "drop table iop_activity_join_info"
db2 -v "create table iop_activity_join_info
(
   iop_activity_id    VARCHAR(50),
   iop_activity_name  VARCHAR(200),
   join_activity_id   VARCHAR(50),
   join_activity_name VARCHAR(200),
   op_time            TIMESTAMP
) in ${tbs}"
db2 -v "comment on column iop_activity_join_info.iop_activity_id is '全网活动id'"
db2 -v "comment on column iop_activity_join_info.iop_activity_name is '全网活动名称'"
db2 -v "comment on column iop_activity_join_info.join_activity_id is '关联活动id'"
db2 -v "comment on column iop_activity_join_info.join_activity_name is '关联活动名称'"
db2 -v "comment on column iop_activity_join_info.op_time is '创建时间'"

#操作日志记录表
db2 -v "drop table operation_log_yyyymm"
db2 -v "create table operation_log_yyyymm
(
   log_id             INT                    not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1 ),
   user_id            VARCHAR(100),
   url                VARCHAR(200),
   http_method        VARCHAR(10),
   class_method       VARCHAR(200),
   parameters         LONG VARCHAR,
   response           CLOB(1048576),
   op_time            TIMESTAMP,
   constraint P_Key_1 primary key (log_id)
)compress yes distribute by hash(log_id) in ${tbs}"
db2 -v "comment on column operation_log_yyyymm.log_id is '日志id'"
db2 -v "comment on column operation_log_yyyymm.user_id is '用户id'"
db2 -v "comment on column operation_log_yyyymm.url is '请求url'"
db2 -v "comment on column operation_log_yyyymm.http_method is 'http请求方法：GET、POST'"
db2 -v "comment on column operation_log_yyyymm.class_method is '调用class方法'"
db2 -v "comment on column operation_log_yyyymm.parameters is '参数列表'"
db2 -v "comment on column operation_log_yyyymm.response is '响应结果'"
db2 -v "comment on column operation_log_yyyymm.op_time is '操作时间'"

#实时位置类型维表
db2 -v "drop table area_type_info "
db2 -v "create table area_type_info
(
   area_id            VARCHAR(50),
   tag_id             VARCHAR(50),
   tag_code           VARCHAR(50),
   tag_name           VARCHAR(50),
   effective          SMALLINT
) in ${tbs}"

db2 -v "comment on column area_type_info.area_id is '位置id' "
db2 -v "comment on column area_type_info.tag_id is '标识id' "
db2 -v "comment on column area_type_info.tag_code is '位置编码' "
db2 -v "comment on column area_type_info.tag_name is '位置名称' "
db2 -v "comment on column area_type_info.effective is '是否有效 0 无效 1 有效' "


#实时位置明细表
db2 -v "drop table area_detail_info "
db2 -v "create table area_detail_info
(
   area_id            VARCHAR(50),
   city_id            VARCHAR(10),
   city_name          VARCHAR(20),
   position_code      VARCHAR(20),
   position_name      VARCHAR(50),
   op_time           	TIMESTAMP,
   effective          SMALLINT
) in ${tbs}   "

db2 -v "comment on column area_detail_info.area_id is '区域id'   "
db2 -v "comment on column area_detail_info.city_id is '地市id'   "
db2 -v "comment on column area_detail_info.city_name is '地市名称'   "
db2 -v "comment on column area_detail_info.position_code is '位置编码'   "
db2 -v "comment on column area_detail_info.position_name is '位置名称'   "
db2 -v "comment on column area_detail_info.op_time is '更新时间'   "
db2 -v "comment on column area_detail_info.effective is '是否有效 0 无效 1有效'   "

#场景渠道实时发送策略表
db2 -tv "drop table scene_channel_business_info"
db2 -v "create table scene_channel_business_info
(
   scene_id           VARCHAR(20),
   scene_name         VARCHAR(20),
   channel_id         VARCHAR(20),
   channel_name       VARCHAR(20),
   city_id            VARCHAR(10),
   city_name          VARCHAR(20),
   interval_day       SMALLINT,
   interval_max_count SMALLINT,
   day_max_count      SMALLINT,
   interval_time      SMALLINT,
   effective          SMALLINT
) in  ${tbs} "
db2 -v "comment on column scene_channel_business_info.scene_id is '场景id'   "
db2 -v "comment on column scene_channel_business_info.scene_name is '场景名称'   "
db2 -v "comment on column scene_channel_business_info.channel_id is '渠道id'   "
db2 -v "comment on column scene_channel_business_info.channel_name is '渠道名称'   "
db2 -v "comment on column scene_channel_business_info.city_id is '地市id'   "
db2 -v "comment on column scene_channel_business_info.city_name is '地市名称'   "
db2 -v "comment on column scene_channel_business_info.interval_day is '几天内可发送'   "
db2 -v "comment on column scene_channel_business_info.interval_max_count is '几天内可接受次数'   "
db2 -v "comment on column scene_channel_business_info.day_max_count is '一天最多接收次数'   "
db2 -v "comment on column scene_channel_business_info.interval_time is '发送有效时间间隔'   "
db2 -v "comment on column scene_channel_business_info.effective is '是否有效'   "

#智能地图task信息表
db2 -v "drop table mental_map_task_info "
db2 -v "create table mental_map_task_info
(
   activity_id            VARCHAR(50),
   activity_name          VARCHAR(50),
   task_id                VARCHAR(50),
   customer_group_name    VARCHAR(50),
   flag                   SMALLINT
) distribute by hash(activity_id) in ${tbs} organize by row"

db2 -v "comment on column mental_map_task_info.activity_id is '活动id' "
db2 -v "comment on column mental_map_task_info.activity_name is '活动名称' "
db2 -v "comment on column mental_map_task_info.task_id is '请求智能地图端的活动id' "
db2 -v "comment on column mental_map_task_info.customer_group_name is '客户群名称' "
db2 -v "comment on column mental_map_task_info.flag is '是否计算完成 0 未完成 1 完成' "

#地市客户群配置表
db2 -v "drop table mental_map_task_info "
db2 -v "create table city_customer_info
(
   city_id            VARCHAR(10),
   city_name          VARCHAR(20),
   dept_id            VARCHAR(10),
   dept_name          VARCHAR(20),
   customer_id        VARCHAR(20),
   customer_name      VARCHAR(50),
   effective          SMALLINT
) in ${tbs}

db2 -v "comment on column city_customer_info.city_id is '地市id' "
db2 -v "comment on column city_customer_info.city_name is '地市名称'  "
db2 -v "comment on column city_customer_info.dept_id is '部门id'  "
db2 -v "comment on column city_customer_info.dept_name is '部门名称' "
db2 -v "comment on column city_customer_info.customer_id is '客户群id' "
db2 -v "comment on column city_customer_info.customer_name is '客户群名称' "
db2 -v "comment on column city_customer_info.effective is '是否有效' "


#部门人员信息表
db2 -v "drop table department_user_info
db2 -v "create table department_user_info
(
   user_id            VARCHAR(20),
   city_id            VARCHAR(20),
   user_name          VARCHAR(50),
   city_name          VARCHAR(50),
   dept_id            VARCHAR(20),
   dept_name          VARCHAR(50),
   effective          SMALLINT
) in ${tbs}
db2 -v "comment on column department_user_info.user_id is '用户id' "
db2 -v "comment on column department_user_info.city_id is '地市id' "
db2 -v "comment on column department_user_info.user_name is '用户名称' "
db2 -v "comment on column department_user_info.city_name is '地市名称' "
db2 -v "comment on column department_user_info.dept_id is '部门id' "
db2 -v "comment on column department_user_info.dept_name is '部门名称' "
db2 -v "comment on column department_user_info.effective is '是否有效' "
