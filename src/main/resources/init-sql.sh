#!/usr/bin/env bash

schema=fcm
tbs=tbs_app_imcd
db2 -v "set current schema ${schema}"

#Ӫ��Ŀ��ά��
db2 -v "drop table marketing_purpose"
db2 -v "create table marketing_purpose
(
   marketing_purpose_id SMALLINT,
   marketing_purpose_name VARCHAR(50),
   effective          SMALLINT
) in ${tbs}"
db2 -v "comment on table marketing_purpose is 'Ӫ��Ŀ��ά��'"
db2 -v "comment on column marketing_purpose.marketing_purpose_id is 'Ӫ��Ŀ��id'"
db2 -v "comment on column marketing_purpose.marketing_purpose_name is 'Ӫ��Ŀ������'"
db2 -v "comment on column marketing_purpose.effective is '�Ƿ���Ч��0��Ч��1��Ч'"
db2 -v "insert into marketing_purpose (marketing_purpose_id,marketing_purpose_name,effective) values (1,'�����ͻ���',1),(2,'����������',1),(3,'��ֵ������',1),(4,'����Ԥ����',1),(9,'������',1)"

#ҵ������ά��
db2 -v "drop table business_type"
db2 -v "create table business_type
(
   business_type_id   SMALLINT,
   business_type_name VARCHAR(50),
   effective          SMALLINT
) in ${tbs}"
db2 -v "comment on table business_type is 'ҵ������ά��'"
db2 -v "comment on column business_type.business_type_id is 'ҵ������id'"
db2 -v "comment on column business_type.business_type_name is 'ҵ����������'"
db2 -v "comment on column business_type.effective is '�Ƿ���Ч��0��Ч��1��Ч'"
db2 -v "insert into business_type (business_type_id,business_type_name,effective) values (1,'4G��Ʒ��',1),(2,'�ն���',1),(3,'������',1),(4,'���ֻ�������',1),(5,'����������',1),(6,'PCC��',1),(7,'�����',1),(9,'������',1)"

#����ά��
db2 -v "drop table city"
db2 -v "create table city
(
   city_id            CHAR(2),
   city_name          VARCHAR(20)
) in ${tbs}"
db2 -v "comment on table city is '����ά��'"
db2 -v "comment on column city.city_id is '����id'"
db2 -v "comment on column city.city_name is '��������'"
db2 -v "insert into city (city_id,city_name) values ('1','ʡ��˾'),('2','�ɶ�'),('3','����'),('4','�Թ�'),('5','��֦��'),('6','��Ԫ'),('7','����'),('8','����'),('9','�㰲'),('10','����'),('11','����'),('12','�˱�'),('13','�ڽ�'),('14','����'),('15','��ɽ'),('16','�Ű�'),('17','����'),('18','�ϳ�'),('19','üɽ'),('20','����'),('21','����'),('22','��ɽ')"

#�ļ��ϴ���Ϣ��
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
db2 -v "comment on table upload_file is '�ļ��ϴ���Ϣ��'"
db2 -v "comment on column upload_file.file_name is '�ļ���'"
db2 -v "comment on column upload_file.img_base64 is 'base64����ͼƬ'"
db2 -v "comment on column upload_file.original_file_name is 'ԭʼ�ļ���'"
db2 -v "comment on column upload_file.uploader_id is '�ϴ���id'"
db2 -v "comment on column upload_file.uploader_name is '�ϴ�������'"
db2 -v "comment on column upload_file.upload_time is '�ϴ�ʱ��'"
db2 -v "comment on column upload_file.use_type is '�ϴ���;��1�����ͻ�Ⱥ��2�����Զ���ͻ�Ⱥ��3�����޳��ÿͻ�Ⱥ��4����ͼƬ'"

#���Ϣ��
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
db2 -v "comment on table activity_info is '���Ϣ��'"
db2 -v "comment on column activity_info.activity_id is '�id'"
db2 -v "comment on column activity_info.activity_name is '�����'"
db2 -v "comment on column activity_info.creator_id is '�����˹���'"
db2 -v "comment on column activity_info.creator_name is '����������'"
db2 -v "comment on column activity_info.create_time is '����ʱ��'"
db2 -v "comment on column activity_info.start_time is '���ʼʱ��'"
db2 -v "comment on column activity_info.end_time is '�����ʱ��'"
db2 -v "comment on column activity_info.city_id is '��������id'"
db2 -v "comment on column activity_info.city_name is '����������'"
db2 -v "comment on column activity_info.dept_id is '����id'"
db2 -v "comment on column activity_info.dept_name is '��������'"
db2 -v "comment on column activity_info.business_type_id is 'ҵ������id'"
db2 -v "comment on column activity_info.business_type_name is 'ҵ��������'"
db2 -v "comment on column activity_info.marketing_purpose_id is 'Ӫ��Ŀ��id'"
db2 -v "comment on column activity_info.marketing_purpose_name is 'Ӫ��Ŀ����'"
db2 -v "comment on column activity_info.scene_flag is '�Ƿ񳡾��'"
db2 -v "comment on column activity_info.scene_id is '����id'"
db2 -v "comment on column activity_info.scene_name is '������'"
db2 -v "comment on column activity_info.remove_employee is '�Ƿ��޳��ڲ����룺0��1��'"
db2 -v "comment on column activity_info.remove_red_list is '�Ƿ��޳���������0��1��'"
db2 -v "comment on column activity_info.remove_sensitive is '�Ƿ��޳������û���0��1��'"
db2 -v "comment on column activity_info.remove_cancel_10086 is '�Ƿ��޳�ȡ��10086���������û���0��1��'"
db2 -v "comment on column activity_info.remove_upload is '�Ƿ��޳��Զ����ϴ��û���0��1��'"
db2 -v "comment on column activity_info.remove_customer_group_id is '�Զ����޳��ͻ�Ⱥid'"
db2 -v "comment on column activity_info.activity_state is '�״̬��0������Ŀ���û���1Ŀ���û����ɳɹ���������������2Ŀ���û�����ʧ�ܣ�3Ŀ���û����ɳɹ��������������ƣ�4��������ͨ����5�����������أ�6��������ͨ����7�����������أ�8�ִ���У�9����'"
db2 -v "comment on column activity_info.stopped is '�Ƿ�������ֹ��0��1��'"
db2 -v "comment on column activity_info.deleted is '�Ƿ���ɾ����0��1��'"
db2 -v "comment on column activity_info.customer_update_cycle is '�ͻ�Ⱥ�������ڣ�1һ���ԣ�2�����ڣ�3������'"

#��Ƽ���Ʒ��
db2 -v "drop table activity_recommend_product"
db2 -v "create table activity_recommend_product
(
   activity_id        VARCHAR(50),
   activity_name      VARCHAR(100),
   product_type       SMALLINT,
   product_id         VARCHAR(50),
   product_name       VARCHAR(100)
) compress yes distribute by hash(activity_id) in ${tbs}"
db2 -v "comment on table activity_recommend_product is '��Ƽ���Ʒ��'"
db2 -v "comment on column activity_recommend_product.activity_id is '�id'"
db2 -v "comment on column activity_recommend_product.activity_name is '�����'"
db2 -v "comment on column activity_recommend_product.product_type is '�Ƽ����ͣ�1�ʷѣ�2Ӫ�����3����'"
db2 -v "comment on column activity_recommend_product.product_id is '�Ƽ���Ʒid'"
db2 -v "comment on column activity_recommend_product.product_name is '�Ƽ���Ʒ����'"

#�ͻ�Ⱥ��Ϣ��
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
db2 -v "comment on table customer_group_info is '�ͻ�Ⱥ��Ϣ��'"
db2 -v "comment on column customer_group_info.customer_group_id is '�ͻ�Ⱥid'"
db2 -v "comment on column customer_group_info.customer_group_name is '�ͻ�Ⱥ��'"
db2 -v "comment on column customer_group_info.group_table_name is '�ͻ�Ⱥ����'"
db2 -v "comment on column customer_group_info.creator_id is '������id'"
db2 -v "comment on column customer_group_info.creator_name is '����������'"
db2 -v "comment on column customer_group_info.create_time is '����ʱ��'"
db2 -v "comment on column customer_group_info.create_type is '������ʽ��1��ǩ�⣬2���ݼ��У�3�ļ��ϴ���4��������'"
db2 -v "comment on column customer_group_info.amount is '�ͻ�����'"
db2 -v "comment on column customer_group_info.customize_flag is '�Ƿ��Զ��壺0��1��'"
db2 -v "comment on column customer_group_info.effective is '�Ƿ���Ч��0��Ч��1��Ч'"
db2 -v "comment on column customer_group_info.remove_flag is '�Ƿ��޳��ÿͻ�Ⱥ��0��1��'"
db2 -v "comment on column customer_group_info.coc_group_id is '��ǩ��ͻ�Ⱥid'"
db2 -v "comment on column customer_group_info.coc_group_name is '��ǩ��ͻ�Ⱥ��'"
db2 -v "comment on column customer_group_info.coc_table_name is '��ǩ��ͻ�Ⱥ����'"
db2 -v "comment on column customer_group_info.coc_group_cycle is '��ǩ��ͻ�Ⱥ���ڣ�1һ���ԣ�2�����ڣ�3������'"
db2 -v "comment on column customer_group_info.mpp_db_name is '�������ݿ���'"
db2 -v "comment on column customer_group_info.mpp_schema is '����schema'"
db2 -v "comment on column customer_group_info.mpp_table_name is '���б���'"
db2 -v "comment on column customer_group_info.mpp_phone_column is '�ֻ����ֶ���'"
db2 -v "comment on column customer_group_info.mpp_marketing_column is '���Ի�Ӫ�����ֶ���'"
db2 -v "comment on column customer_group_info.file_name is '�ļ���'"
db2 -v "comment on column customer_group_info.original_file_name is 'ԭʼ�ļ���'"
db2 -v "comment on column customer_group_info.file_line_count is 'ԭʼ�ļ���¼��'"
db2 -v "comment on column customer_group_info.migu_content_id is '��������id'"
db2 -v "comment on column customer_group_info.migu_content_name is '������������'"
db2 -v "comment on column customer_group_info.migu_content_type is '�����������ͣ�1�����Ƽ��û���2�û��Ƽ�����-��ǩ��3�û��Ƽ�����-�ϴ�'"
db2 -v "comment on column customer_group_info.migu_content_marketing is '��������Ӫ����'"
db2 -v "comment on column customer_group_info.migu_content_shorturl is '�������ݶ����ӵ�ַ'"
db2 -v "comment on column customer_group_info.migu_content_completed is '�������ݽ���Ƿ������ɣ�0δ��ɣ�1�����'"
db2 -v "comment on column customer_group_info.migu_content_outputpath is '�������ݽ�������ַ'"
db2 -v "comment on column customer_group_info.data_time is '����ʱ��'"
db2 -v "comment on column customer_group_info.data_state is '����״̬��0δ���أ�1�������'"

#����Ժ����
db2 -v "drop table activity_test_phone"
db2 -v "create table activity_test_phone
(
   activity_id        VARCHAR(50),
   test_phone_no      VARCHAR(20)
) compress yes distribute by hash(activity_id) in ${tbs}"
db2 -v "comment on table activity_test_phone is '����Ժ����'"
db2 -v "comment on column activity_test_phone.activity_id is '�id'"
db2 -v "comment on column activity_test_phone.test_phone_no is '���Ժ���'"

#����ά��
db2 -v "drop table scene"
db2 -v "create table scene
(
   scene_id           SMALLINT,
   scene_name         VARCHAR(50),
   effective          SMALLINT
) in ${tbs}"
db2 -v "comment on table scene is '����ά��'"
db2 -v "comment on column scene.scene_id is '����id'"
db2 -v "comment on column scene.scene_name is '������'"
db2 -v "comment on column scene.effective is '�Ƿ���Ч��0��Ч��1��Ч'"
db2 -v "insert into scene (scene_id,scene_name,effective) values (1,'4G����',1),(2,'�����ɷ�',1),(3,'ҵ�񶩹�',1),(4,'ʵʱλ��',1),(5,'������Ϊ',1),(6,'�Ͱ�ʹ��Ӫ��',1)"

#��������ά��
db2 -v "drop table channel_type"
db2 -v "create table channel_type
(
   channel_type_id    SMALLINT,
   channel_type_name  VARCHAR(50)
) in ${tbs}"
db2 -v "comment on table channel_type is '��������ά��'"
db2 -v "comment on column channel_type.channel_type_id is '��������id'"
db2 -v "comment on column channel_type.channel_type_name is '������������'"
db2 -v "insert into channel_type (channel_type_id,channel_type_name) values (1,'Ӫҵǰ̨��'),(2,'������'),(3,'������������')"

#������Ϣ��
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
db2 -v "comment on table channel is '������Ϣ��'"
db2 -v "comment on column channel.channel_id is '����id'"
db2 -v "comment on column channel.channel_name is '��������'"
db2 -v "comment on column channel.channel_type_id is '��������id'"
db2 -v "comment on column channel.channel_type_name is '������������'"
db2 -v "comment on column channel.need_content_approval is '�Ƿ���Ҫ����������0����Ҫ��1��Ҫ'"
db2 -v "comment on column channel.need_channel_approval is '�Ƿ���Ҫ����������0����Ҫ��1��Ҫ'"
db2 -v "comment on column channel.need_leader_approval is '�Ƿ���Ҫ�ֹ��쵼������0����Ҫ��1��Ҫ'"
db2 -v "comment on column channel.is_can_sms_approval is '�Ƿ�֧�ֶ���������0 ��֧�� 1֧��'"
db2 -v "comment on column channel.effective is '�Ƿ���Ч��0��Ч��1��Ч'"
db2 -v "comment on column channel.order_id is '�����ֶΣ���������'"
db2 -v "insert into channel (channel_id,channel_name,channel_type_id,channel_type_name,need_content_approval,need_channel_approval,need_leader_approval,effective,order_id) values ('f01','ǰ̨����/���ƹ�',1,'Ӫҵǰ̨��',1,0,0,1,1),('f02','�ͷ�ϵͳ',1,'Ӫҵǰ̨��',1,1,0,1,2),('d01','���ϳ���',2,'������',1,1,0,1,3),('d02','�������ģʽ',2,'������',1,1,1,1,4),('d03','��������ģʽ',2,'������',1,1,1,1,5),('q03','����',3,'������������',1,1,0,1,9),('q05','������',3,'������������',1,1,0,1,8),('q07','��������������',3,'������������',1,1,0,1,10),('q09','׷β����ģʽ',2,'������',1,1,1,1,6)"

#���������ñ�
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

db2 -v "comment on table approver_info is '���������ñ�'   "
db2 -v "comment on column approver_info.uuid is '����������'   "
db2 -v "comment on column approver_info.approver_id is '������id'   "
db2 -v "comment on column approver_info.approver_name is '����������'   "
db2 -v "comment on column approver_info.approver_phone is '�������ֻ�'   "
db2 -v "comment on column approver_info.approver_level is '�����˼���'   "
db2 -v "comment on column approver_info.approval_group is '����������������'   "
db2 -v "comment on column approver_info.approval_role is '�����˽�ɫ'   "
db2 -v "comment on column approver_info.is_receive_sms is '�Ƿ����������Ϣ 0�� 1��'   "
db2 -v "comment on column approver_info.effective is '�Ƿ���Ч��0��Ч��1��Ч'   "
#����ȼ���Ϣ��
db2 -v "drop table activity_priority"
db2 -v "create table activity_priority
(
   activity_id        VARCHAR(50),
   activity_name      VARCHAR(100),
   city_id            CHAR(2),
   city_name          VARCHAR(20),
   priority_time      TIMESTAMP
) compress yes distribute by hash(activity_id) in ${tbs}"
db2 -v "comment on table activity_priority is '����ȼ���Ϣ��'"
db2 -v "comment on column activity_priority.activity_id is '�id'"
db2 -v "comment on column activity_priority.activity_name is '�����'"
db2 -v "comment on column activity_priority.city_id is '�����id'"
db2 -v "comment on column activity_priority.city_name is '��������'"
db2 -v "comment on column activity_priority.priority_time is '���ȼ�����ʱ�䣨ֵԽ�����ȼ�Խ�ߣ��½�Ĭ�ϴ���ʱ�䣩'"

#�������Ϣ��
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
db2 -v " comment on table activity_approval_info is '�������¼��'"
db2 -v " comment on column activity_approval_info.activity_id is '�id'"
db2 -v " comment on column activity_approval_info.activity_name is '�����'"
db2 -v " comment on column activity_approval_info.channel_id is '����id'"
db2 -v " comment on column activity_approval_info.channel_name is '��������'"
db2 -v " comment on column activity_approval_info.approval_result is '���������0δ������1����ͨ����2��������'"
db2 -v " comment on column activity_approval_info.approval_comments is '�������'"
db2 -v " comment on column activity_approval_info.approver_id is '������id'"
db2 -v " comment on column activity_approval_info.approver_name is '����������'"
db2 -v " comment on column activity_approval_info.approver_phone is '�������ֻ���'"
db2 -v " comment on column activity_approval_info.approver_level is '�����˲㼶��1����������2����������3�ֹ��쵼����'"
db2 -v " comment on column activity_approval_info.approve_time is '����ʱ��'"

#���г������ñ�
db2 -v "drop table city_scene_info"
db2 -v "create table city_scene_info
(
   city_id            CHAR(2),
   city_name          VARCHAR(20),
   scene_id           SMALLINT,
   scene_name         VARCHAR(50),
   effective          SMALLINT
) in ${tbs}"
db2 -v "comment on table city_scene_info is '���г������ñ�'"
db2 -v "comment on column city_scene_info.city_id is '����id'"
db2 -v "comment on column city_scene_info.city_name is '��������'"
db2 -v "comment on column city_scene_info.scene_id is '����id'"
db2 -v "comment on column city_scene_info.scene_name is '��������'"
db2 -v "comment on column city_scene_info.effective is '�Ƿ���Ч��0��Ч��1��Ч'"


#�����������ñ�
db2 -v "drop table scene_channel_info"
db2 -v "create table scene_channel_info
(
   scene_id           SMALLINT,
   scene_name         VARCHAR(50),
   channel_id         VARCHAR(10),
   channel_name       VARCHAR(50),
   effective          SMALLINT
) in ${tbs}"
db2 -v "comment on table scene_channel_info is '�����������ñ�'"
db2 -v "comment on column scene_channel_info.scene_id is '����id'"
db2 -v "comment on column scene_channel_info.scene_name is '��������'"
db2 -v "comment on column scene_channel_info.channel_id is '����id'"
db2 -v "comment on column scene_channel_info.channel_name is '��������'"
db2 -v "comment on column scene_channel_info.effective is '�Ƿ���Ч��0��Ч��1��Ч'"

#�����������ñ�
db2 -v "drop table city_channel_info"
db2 -v "create table city_channel_info
(
   city_id            CHAR(2),
   city_name          VARCHAR(20),
   channel_id         VARCHAR(10),
   channel_name       VARCHAR(50),
   effective          SMALLINT
) in ${tbs}"
db2 -v "comment on table city_channel_info is '�����������ñ�'"
db2 -v "comment on column city_channel_info.city_id is '����id'"
db2 -v "comment on column city_channel_info.city_name is '��������'"
db2 -v "comment on column city_channel_info.channel_id is '����id'"
db2 -v "comment on column city_channel_info.channel_name is '��������'"
db2 -v "comment on column city_channel_info.effective is '�Ƿ���Ч'"
db2 -v "insert into city_channel_info select a.city_id,a.city_name,b.channel_id,b.channel_name,1 effective from city a left join channel b on 1=1"

#��ͻ�Ⱥ��Ϣ��
db2 -v "drop table activity_customer_group_info"
db2 -v "create table activity_customer_group_info
(
   activity_id        VARCHAR(50),
   activity_name      VARCHAR(100),
   customer_group_id  VARCHAR(50),
   customer_group_name VARCHAR(100),
   create_type        SMALLINT
) compress yes distribute by hash(activity_id) in ${tbs}"
db2 -v "comment on table activity_customer_group_info is '��ͻ�Ⱥ��Ϣ��'"
db2 -v "comment on column activity_customer_group_info.activity_id is '�id'"
db2 -v "comment on column activity_customer_group_info.activity_name is '�����'"
db2 -v "comment on column activity_customer_group_info.customer_group_id is '�ͻ�Ⱥid'"
db2 -v "comment on column activity_customer_group_info.customer_group_name is '�ͻ�Ⱥ����'"
db2 -v "comment on column activity_customer_group_info.create_type is '������ʽ��1��ǩ�⣬2���ݼ��У�3�ļ��ϴ���4��������'"

#�Ŀ���û�������Ϣ��
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
db2 -v "comment on table activity_customer_remove_info is '�Ŀ���û�������Ϣ��'"
db2 -v "comment on column activity_customer_remove_info.activity_id is '�id'"
db2 -v "comment on column activity_customer_remove_info.activity_name is '�����'"
db2 -v "comment on column activity_customer_remove_info.original_amount is 'ԭʼ�ͻ���'"
db2 -v "comment on column activity_customer_remove_info.remove_employee is '�Ƿ��޳��ڲ�Ա����0��1��'"
db2 -v "comment on column activity_customer_remove_info.remove_employee_amount is '�޳��ڲ�Ա����'"
db2 -v "comment on column activity_customer_remove_info.remove_red_list is '�Ƿ��޳���������0��1��'"
db2 -v "comment on column activity_customer_remove_info.remove_red_list_amount is '�޳��������û���'"
db2 -v "comment on column activity_customer_remove_info.remove_sensitive is '�Ƿ��޳������û���0��1��'"
db2 -v "comment on column activity_customer_remove_info.remove_sensitive_amount is '�޳������û���'"
db2 -v "comment on column activity_customer_remove_info.remove_cancel_10086 is '�Ƿ��޳�ȡ��10086���������û���0��1��'"
db2 -v "comment on column activity_customer_remove_info.remove_cancel_10086_amount is '�޳�ȡ��10086���������û���'"
db2 -v "comment on column activity_customer_remove_info.remove_upload is '�Ƿ��޳��Զ����û���0��1��'"
db2 -v "comment on column activity_customer_remove_info.remove_upload_amount is '�޳��Զ����û���'"
db2 -v "comment on column activity_customer_remove_info.customize_flag is '�Ƿ��Զ���Ӫ���0��1��'"
db2 -v "comment on column activity_customer_remove_info.final_group_table_name is '���տͻ�Ⱥ����'"
db2 -v "comment on column activity_customer_remove_info.final_amount is '�����û���'"
db2 -v "comment on column activity_customer_remove_info.state is 'Ŀ���û�����״̬��0δ���ɣ�1���ɳɹ���2����ʧ��'"
db2 -v "comment on column activity_customer_remove_info.cycle_update_completed is '���ڸ�����ɱ�ʾ��0δ��ɣ�1�����'"
db2 -v "comment on column activity_customer_remove_info.exception_info is '������Ϣ'"

#��������ά��
db2 -v "drop table channel_rule"
db2 -v "create table channel_rule
(
   rule_id            CHAR(4),
   rule_name          VARCHAR(100)
) in ${tbs}"
db2 -v "comment on table channel_rule is '��������ά��'"
db2 -v "comment on column channel_rule.rule_id is '��������id'"
db2 -v "comment on column channel_rule.rule_name is '������������'"
db2 -v "insert into channel_rule(rule_id,rule_name) values ('R001','Ӫ������/��������'),('R002','��Ӫλid'),('R003','��Ӫλ����'),('R004','��ӪλĿ¼'),('R005','ͼƬ'),('R006','ͼƬ����'),('R007','��ת����'),('R008','��������'),('R009','����С��'),('R010','�������'),('R011','����')"

#�������Ϣ��
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
db2 -v "comment on table activity_channel_info is '�������Ϣ��'"
db2 -v "comment on column activity_channel_info.activity_id is '�id'"
db2 -v "comment on column activity_channel_info.activity_name is '�����'"
db2 -v "comment on column activity_channel_info.channel_id is '����id'"
db2 -v "comment on column activity_channel_info.channel_name is '��������'"
db2 -v "comment on column activity_channel_info.rule_id is '��������id'"
db2 -v "comment on column activity_channel_info.rule_value is '��������ֵ'"
db2 -v "comment on column activity_channel_info.rule_extended_value is '����������չֵ'"

#�����
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
db2 -v "comment on table task is '�����'"
db2 -v "comment on column task.task_id is '����id'"
db2 -v "comment on column task.task_name is '��������'"
db2 -v "comment on column task.creator_id is '������id'"
db2 -v "comment on column task.creator_name is '����������'"
db2 -v "comment on column task.create_time is '����ʱ��'"
db2 -v "comment on column task.start_time is '��ʼʱ��'"
db2 -v "comment on column task.end_time is '����ʱ��'"
db2 -v "comment on column task.city_id is '����id'"
db2 -v "comment on column task.city_name is '��������'"
db2 -v "comment on column task.business_type_id is 'ҵ������id'"
db2 -v "comment on column task.business_type_name is 'ҵ��������'"
db2 -v "comment on column task.marketing_purpose_id is 'Ӫ��Ŀ��id'"
db2 -v "comment on column task.marketing_purpose_name is 'Ӫ��Ŀ����'"
db2 -v "comment on column task.effective is '�Ƿ���Ч��0��Ч��1��Ч'"

#������
db2 -v "drop table task_activity"
db2 -v "create table task_activity
(
   task_id            VARCHAR(50),
   task_name          VARCHAR(100),
   activity_id        VARCHAR(50),
   activity_name      VARCHAR(100)
) compress yes distribute by hash(task_id) in ${tbs}"
db2 -v "comment on table task_activity is '������'"
db2 -v "comment on column task_activity.task_id is '����id'"
db2 -v "comment on column task_activity.task_name is '��������'"
db2 -v "comment on column task_activity.activity_id is '�id'"
db2 -v "comment on column task_activity.activity_name is '�����'"

#�����������ñ�
db2 -v "drop table scene_rule"
db2 -v "create table scene_rule
(
   scene_rule_id      CHAR(5),
   scene_rule_name    VARCHAR(100)
) in ${tbs}"
db2 -v "comment on table scene_rule is '�����������ñ�'"
db2 -v "comment on column scene_rule.scene_rule_id is '��������id'"
db2 -v "comment on column scene_rule.scene_rule_name is '������������'"

#�������Ϣ��
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
db2 -v "comment on table activity_scene_info is '�������Ϣ��'"
db2 -v "comment on column activity_scene_info.activity_id is '�id'"
db2 -v "comment on column activity_scene_info.activity_name is '�����'"
db2 -v "comment on column activity_scene_info.scene_id is '����id'"
db2 -v "comment on column activity_scene_info.scene_name is '��������'"
db2 -v "comment on column activity_scene_info.scene_rule_id is '��������id'"
db2 -v "comment on column activity_scene_info.scene_rule_value is '��������ֵ'"
db2 -v "comment on column activity_scene_info.scene_rule_extended_value is '����������չֵ'"

#������Ϊapp�������ñ�
db2 -v "drop table app_type_define"
db2 -v "create table app_type_define
(
   app_type_id           INTEGER,
   app_type_name      VARCHAR(50),
   app_type_desc      VARCHAR(50),
   effective          INTEGER
) in ${tbs}"
db2 -v "comment on column app_type_define.app_type_id is 'app����'"
db2 -v "comment on column app_type_define.app_type_name is 'app��������'"
db2 -v "comment on column app_type_define.app_type_desc is 'app��������'"
db2 -v "comment on column app_type_define.effective is '0 ��Ч 1 ��Ч'"

#����������¼��
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
db2 -v "comment on column activity_approval_sms_info.sms_code is '���ŷ�����'"
db2 -v "comment on column activity_approval_sms_info.activity_id is '�id'"
db2 -v "comment on column activity_approval_sms_info.activity_name is '�����'"
db2 -v "comment on column activity_approval_sms_info.channel_id is '����id'"
db2 -v "comment on column activity_approval_sms_info.channel_name is '��������'"
db2 -v "comment on column activity_approval_sms_info.activity_content is '�����'"
db2 -v "comment on column activity_approval_sms_info.approver_name is '������'"
db2 -v "comment on column activity_approval_sms_info.approver_level is '�����˲㼶'"
db2 -v "comment on column activity_approval_sms_info.approver_phone is '�����˵绰'"
db2 -v "comment on column activity_approval_sms_info.approval_type is '1�������� 2��������'"
db2 -v "comment on column activity_approval_sms_info.sms_approval_result is '0δ���� 1����ͨ�� 2��������'"
db2 -v "comment on column activity_approval_sms_info.sms_send_time is '���ŷ���ʱ��'"
db2 -v "comment on column activity_approval_sms_info.sms_send_state is '���ŷ���״̬'"

#���ŷ���ģ���
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
db2 -v "comment on column sms_template.template_id is 'ģ��id'"
db2 -v "comment on column sms_template.template_type is 'ģ�����ͣ�1����ģ�� 2����ͨ��ģ�� 3��������ģ��'"
db2 -v "comment on column sms_template.channel_id is '����id'"
db2 -v "comment on column sms_template.approval_level is '�����㼶'"
db2 -v "comment on column sms_template.template_content is 'ģ������'"
db2 -v "comment on column sms_template.effective is '�Ƿ���Ч 0��Ч  1��Ч'"

# �û�Ȩ����Ϣ��
db2 -v "drop table user_role_info"
db2 -v "create table user_role_info
(
   user_id            VARCHAR(50),
   user_role          VARCHAR(20)
) in ${tbs}"
db2 -v "comment on column user_role_info.user_id is '�û�id'"
db2 -v "comment on column user_role_info.user_role is '�û���ɫ'"

#�Ӫ���û�����״̬��
db2 -v "drop table activity_customers_build_state"
db2 -v "create table activity_customers_build_state
(
   activity_id        VARCHAR(50),
   activity_name      VARCHAR(100),
   source_is_complete SMALLINT,
   user_file_dir      VARCHAR(200),
   is_complete        SMALLINT
) compress yes distribute by hash(activity_id) in ${tbs}"
db2 -v "comment on column activity_customers_build_state.activity_id is '�id'"
db2 -v "comment on column activity_customers_build_state.activity_name is '�����'"
db2 -v "comment on column activity_customers_build_state.source_is_complete is 'ԴĿ���û��Ƿ� 0δ���� 1������'"
db2 -v "comment on column activity_customers_build_state.user_file_dir is 'Ŀ���û��ļ�����·��'"
db2 -v "comment on column activity_customers_build_state.is_complete is '�Ƿ����� 0δ���� 1������'"

#ȫ���������Ϣ
db2 -v "drop table iop_activity_join_info"
db2 -v "create table iop_activity_join_info
(
   iop_activity_id    VARCHAR(50),
   iop_activity_name  VARCHAR(200),
   join_activity_id   VARCHAR(50),
   join_activity_name VARCHAR(200),
   op_time            TIMESTAMP
) in ${tbs}"
db2 -v "comment on column iop_activity_join_info.iop_activity_id is 'ȫ���id'"
db2 -v "comment on column iop_activity_join_info.iop_activity_name is 'ȫ�������'"
db2 -v "comment on column iop_activity_join_info.join_activity_id is '�����id'"
db2 -v "comment on column iop_activity_join_info.join_activity_name is '���������'"
db2 -v "comment on column iop_activity_join_info.op_time is '����ʱ��'"

#������־��¼��
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
db2 -v "comment on column operation_log_yyyymm.log_id is '��־id'"
db2 -v "comment on column operation_log_yyyymm.user_id is '�û�id'"
db2 -v "comment on column operation_log_yyyymm.url is '����url'"
db2 -v "comment on column operation_log_yyyymm.http_method is 'http���󷽷���GET��POST'"
db2 -v "comment on column operation_log_yyyymm.class_method is '����class����'"
db2 -v "comment on column operation_log_yyyymm.parameters is '�����б�'"
db2 -v "comment on column operation_log_yyyymm.response is '��Ӧ���'"
db2 -v "comment on column operation_log_yyyymm.op_time is '����ʱ��'"

#ʵʱλ������ά��
db2 -v "drop table area_type_info "
db2 -v "create table area_type_info
(
   area_id            VARCHAR(50),
   tag_id             VARCHAR(50),
   tag_code           VARCHAR(50),
   tag_name           VARCHAR(50),
   effective          SMALLINT
) in ${tbs}"

db2 -v "comment on column area_type_info.area_id is 'λ��id' "
db2 -v "comment on column area_type_info.tag_id is '��ʶid' "
db2 -v "comment on column area_type_info.tag_code is 'λ�ñ���' "
db2 -v "comment on column area_type_info.tag_name is 'λ������' "
db2 -v "comment on column area_type_info.effective is '�Ƿ���Ч 0 ��Ч 1 ��Ч' "


#ʵʱλ����ϸ��
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

db2 -v "comment on column area_detail_info.area_id is '����id'   "
db2 -v "comment on column area_detail_info.city_id is '����id'   "
db2 -v "comment on column area_detail_info.city_name is '��������'   "
db2 -v "comment on column area_detail_info.position_code is 'λ�ñ���'   "
db2 -v "comment on column area_detail_info.position_name is 'λ������'   "
db2 -v "comment on column area_detail_info.op_time is '����ʱ��'   "
db2 -v "comment on column area_detail_info.effective is '�Ƿ���Ч 0 ��Ч 1��Ч'   "

#��������ʵʱ���Ͳ��Ա�
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
db2 -v "comment on column scene_channel_business_info.scene_id is '����id'   "
db2 -v "comment on column scene_channel_business_info.scene_name is '��������'   "
db2 -v "comment on column scene_channel_business_info.channel_id is '����id'   "
db2 -v "comment on column scene_channel_business_info.channel_name is '��������'   "
db2 -v "comment on column scene_channel_business_info.city_id is '����id'   "
db2 -v "comment on column scene_channel_business_info.city_name is '��������'   "
db2 -v "comment on column scene_channel_business_info.interval_day is '�����ڿɷ���'   "
db2 -v "comment on column scene_channel_business_info.interval_max_count is '�����ڿɽ��ܴ���'   "
db2 -v "comment on column scene_channel_business_info.day_max_count is 'һ�������մ���'   "
db2 -v "comment on column scene_channel_business_info.interval_time is '������Чʱ����'   "
db2 -v "comment on column scene_channel_business_info.effective is '�Ƿ���Ч'   "

#���ܵ�ͼtask��Ϣ��
db2 -v "drop table mental_map_task_info "
db2 -v "create table mental_map_task_info
(
   activity_id            VARCHAR(50),
   activity_name          VARCHAR(50),
   task_id                VARCHAR(50),
   customer_group_name    VARCHAR(50),
   flag                   SMALLINT
) distribute by hash(activity_id) in ${tbs} organize by row"

db2 -v "comment on column mental_map_task_info.activity_id is '�id' "
db2 -v "comment on column mental_map_task_info.activity_name is '�����' "
db2 -v "comment on column mental_map_task_info.task_id is '�������ܵ�ͼ�˵Ļid' "
db2 -v "comment on column mental_map_task_info.customer_group_name is '�ͻ�Ⱥ����' "
db2 -v "comment on column mental_map_task_info.flag is '�Ƿ������� 0 δ��� 1 ���' "

#���пͻ�Ⱥ���ñ�
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

db2 -v "comment on column city_customer_info.city_id is '����id' "
db2 -v "comment on column city_customer_info.city_name is '��������'  "
db2 -v "comment on column city_customer_info.dept_id is '����id'  "
db2 -v "comment on column city_customer_info.dept_name is '��������' "
db2 -v "comment on column city_customer_info.customer_id is '�ͻ�Ⱥid' "
db2 -v "comment on column city_customer_info.customer_name is '�ͻ�Ⱥ����' "
db2 -v "comment on column city_customer_info.effective is '�Ƿ���Ч' "


#������Ա��Ϣ��
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
db2 -v "comment on column department_user_info.user_id is '�û�id' "
db2 -v "comment on column department_user_info.city_id is '����id' "
db2 -v "comment on column department_user_info.user_name is '�û�����' "
db2 -v "comment on column department_user_info.city_name is '��������' "
db2 -v "comment on column department_user_info.dept_id is '����id' "
db2 -v "comment on column department_user_info.dept_name is '��������' "
db2 -v "comment on column department_user_info.effective is '�Ƿ���Ч' "
