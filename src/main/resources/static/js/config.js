/**
 * 营销活动详情展示，前台模板集合
 */
var globalConfig = {
	excChannelListItem: {
		mainQueryUrl: "getExcChannelListItemInfo",
		exc_channel_1: {
			template: "",
			secondQueryUrl: "getExcChannel1Info",
			dealGotData: function(){},

		},
		exc_channel_11: {
			template: "",
			secondQueryUrl: "getExcChannel2Info",
			dealGotData: function(){},
		}
	},
	imports: {
	},
	detailChannelTab: {
		"f01": {
			channelName: "前台弹窗/大掌柜",
			channelTabNavItem: '<a href="#f01">前台弹窗/大掌柜</a>',
			template: '<!-- 前台弹窗/大掌柜 -->'+
								'<li data-channelid="f01">'+
								'	<div class="row">'+
								'		<span class="item-property">营销用语：</span>'+
								'		<span data-elemid="R001">无</span>'+
								'	</div>'+
								'	<div class="row approver-box">'+
								'		<div class="col-1">'+
								'			<span class="item-property">一级审批：</span>'+
								'			<span data-id="contentApproverId">无</span>'+
								'		</div>'+
								'		<div class="col-1">'+
								'			<span class="item-property">二级审批：</span>'+
								'			<span data-id="channelApproverId">无</span>'+
								'		</div>'+
								'	</div>'+
								'</li>'
		},
		"f02": {
			channelName: "客服系统",
			channelTabNavItem: '<a href="#f02">客服系统</a>',
			template: '<!-- 客服系统 -->'+
								'<li data-channelid="f02">'+
								'	<div class="row">'+
								'		<span class="item-property">营销用语：</span>'+
								'		<span data-elemid="R001">无</span>'+
								'	</div>'+
								'	<div class="row approver-box">'+
								'		<div class="col-1">'+
								'			<span class="item-property">一级审批：</span>'+
								'			<span data-id="contentApproverId">无</span>'+
								'		</div>'+
								'		<div class="col-1">'+
								'			<span class="item-property">二级审批：</span>'+
								'			<span data-id="channelApproverId">无</span>'+
								'		</div>'+
								'	</div>'+
								'</li>'
		},
		"q03": {
			channelName: "掌厅",
			channelTabNavItem: '<a href="#q03">掌厅</a>',
			template: '<!-- 掌厅 -->'+
								'<li data-channelid="q03">'+
								'	<div class="row approver-box">'+
								'		<div class="col-1">'+
								'			<span class="item-property">广告位：</span>'+
								'			<span data-elemid="R002">无</span>'+
								'		</div>'+
								'		<div class="col-1">'+
								'			<span class="item-property">连接码表：</span>'+
								'			<span data-elemid="R010">无</span>'+
								'		</div>'+
								'	</div> '+
								'	<div class="row">'+
								'		<span class="item-property">URL：</span>'+
								'		<span data-elemid="R007">无</span>'+
								'	</div>'+
								'	<div class="row">'+
								'		<span class="item-property">标题：</span>'+
								'		<span data-elemid="R011">无</span>'+
								'	</div>'+
								'	<div class="row">'+
								'		<span class="item-property">内容：</span>'+
								'		<span data-elemid="R001">无</span>'+
								'	</div>'+
								'	<div class="row">'+
								'		<span class="item-property" data-elemid="R005">图片：</span>'+
								'		<span >无</span>'+
								'	</div>'+
								'	<div class="row approver-box">'+
								'		<div class="col-1">'+
								'			<span class="item-property">一级审批：</span>'+
								'			<span data-id="contentApproverId">无</span>'+
								'		</div>'+
								'		<div class="col-1">'+
								'			<span class="item-property">二级审批：</span>'+
								'			<span data-id="channelApproverId">无</span>'+
								'		</div>'+
								'	</div>'+
								'</li>'
		},
		"d01": {
			channelName: "优惠提醒",
			channelTabNavItem: '<a href="#d01">优惠提醒</a>',
			template: '<!-- 优惠提醒 -->'+
								'<li data-channelid="d01">'+
								'	<div class="row">'+
								'		<span class="item-property">短信下发内容：</span>'+
								'		<span data-elemid="R001">无</span>'+
								'	</div>'+
								'	<div class="row approver-box">'+
								'		<div class="col-1">'+
								'			<span class="item-property">一级审批：</span>'+
								'			<span data-id="contentApproverId">无</span>'+
								'		</div>'+
								'		<div class="col-1">'+
								'			<span class="item-property">二级审批：</span>'+
								'			<span data-id="channelApproverId">无</span>'+
								'		</div>'+
								'	</div>'+
								'</li>'
		},
		"d02": {
			channelName: "触点短信模式",
			channelTabNavItem: '<a href="#d02">触点短信模式</a>',
			template: '<!-- 触点短信模式 -->'+
								'<li data-channelid="d02">'+
								'	<div class="row">'+
								'		<span class="item-property">渠道小类：</span>'+
								'		<span data-elemid="R009">无</span>'+
								'	</div>'+
								'	<div class="row">'+
								'		<span class="item-property">短信挂尾内容：</span>'+
								'		<span data-elemid="R001">无</span>'+
								'	</div>'+
								'	<div class="row approver-box">'+
								'		<div class="col-1">'+
								'			<span class="item-property">一级审批：</span>'+
								'			<span data-id="contentApproverId">无</span>'+
								'		</div>'+
								'		<div class="col-1">'+
								'			<span class="item-property">二级审批：</span>'+
								'			<span data-id="channelApproverId">无</span>'+
								'		</div>'+
								'	</div>'+
								'</li>'
		},
		"d03": {
			channelName: "主动宣传模式",
			channelTabNavItem: '<a href="#d03">主动宣传模式</a>',
			template: '<!-- 主动宣传模式 -->'+
								'<li data-channelid="d03">'+
								'	<div class="row">'+
								'		<span class="item-property">短信挂尾内容：</span>'+
								'		<span data-elemid="R001">无</span>'+
								'	</div>'+
								'	<div class="row approver-box">'+
								'		<div class="col-1">'+
								'			<span class="item-property">一级审批：</span>'+
								'			<span data-id="contentApproverId">无</span>'+
								'		</div>'+
								'		<div class="col-1">'+
								'			<span class="item-property">二级审批：</span>'+
								'			<span data-id="channelApproverId">无</span>'+
								'		</div>'+
								'	</div>'+
								'</li>'
		},
		"d05": {
			channelName: "10086群发",
			channelTabNavItem: '<a href="#d05">10086群发</a>',
			template: '<!-- 10086群发 -->'+
			'<li data-channelid="d05">'+
			'	<div class="row">'+
			'		<span class="item-property">营销用语：</span>'+
			'		<span data-elemid="R001">无</span>'+
			'	</div>'+
			'	<div class="row approver-box">'+
			'		<div class="col-1">'+
			'			<span class="item-property">一级审批：</span>'+
			'			<span data-id="channelApproverId">无</span>'+
			'		</div>'+
			'		<div class="col-1">'+
			'			<span class="item-property">二级审批：</span>'+
			'			<span data-id="contentApproverId">无</span>'+
			'		</div>'+
			'	</div>'+
			'</li>'
		},
		"d06": {
			channelName: "来电提醒",
			channelTabNavItem: '<a href="#d06">来电提醒</a>',
			template: '<!-- 来电提醒 -->'+
			'<li data-channelid="d06">'+
			'	<div class="row">'+
			'		<span class="item-property">营销用语：</span>'+
			'		<span data-elemid="R001">无</span>'+
			'	</div>'+
			'	<div class="row approver-box">'+
			'		<div class="col-1">'+
			'			<span class="item-property">一级审批：</span>'+
			'			<span data-id="contentApproverId">无</span>'+
			'		</div>'+
			'		<div class="col-1">'+
			'			<span class="item-property">二级审批：</span>'+
			'			<span data-id="channelApproverId">无</span>'+
			'		</div>'+
			'	</div>'+
			'</li>'
		},
		"d07": {
			channelName: "外呼",
			channelTabNavItem: '<a href="#d07">外呼</a>',
			template: '<!-- 来电提醒 -->'+
			'<li data-channelid="d07">'+
            '	<div class="row">'+
            '		<span class="item-property">渠道小类：</span>'+
            '		<span data-elemid="R009">无</span>'+
            '	</div>'+
			'	<div class="row">'+
			'		<span class="item-property">营销用语：</span>'+
			'		<span data-elemid="R001">无</span>'+
			'	</div>'+
			'	<div class="row approver-box">'+
			'		<div class="col-1">'+
			'			<span class="item-property">一级审批：</span>'+
			'			<span data-id="contentApproverId">无</span>'+
			'		</div>'+
			'		<div class="col-1">'+
			'			<span class="item-property">二级审批：</span>'+
			'			<span data-id="channelApproverId">无</span>'+
			'		</div>'+
			'	</div>'+
			'</li>'
		},
		"q09": {
			channelName: "追尾短信模式",
			channelTabNavItem: '<a href="#q09">追尾短信模式</a>',
			template: '<!-- 追尾短信模式 -->'+
								'<li data-channelid="q09">'+
								'	<div class="row">'+
								'		<div class="col-1" >'+
								'			<span class="item-property" >提醒类型：</span>'+
								'			<span data-elemid="R008">无</span>'+
								'		</div>'+
								'	</div>'+
								'	<div class="row">'+
								'		<span class="item-property col-long">营销用语：</span>'+
								'		<span data-elemid="R001" class="col-long-value">无</span>'+
								'	</div>'+
								'	<div class="row approver-box">'+
								'		<div class="col-1">'+
								'			<span class="item-property">一级审批：</span>'+
								'			<span data-id="contentApproverId">无</span>'+
								'		</div>'+
								'		<div class="col-1">'+
								'			<span class="item-property">二级审批：</span>'+
								'			<span data-id="channelApproverId">无</span>'+
								'		</div>'+
								'	</div>'+
								'</li>'
		},
		"q07": {
			channelName: "互联网渠道",
			channelTabNavItem: '<a href="#q07">互联网渠道</a>',
			template: '<!-- 互联网渠道 -->'+
								'<li data-channelid="q07">'+
								'	<div class="row">'+
								'		<div class="col-1" >'+
								'			<span class="item-property" >运营位名称：</span>'+
								'			<span data-elemid="R002">无</span>'+
								'		</div>'+
								'		<div class="col-1">'+
								'			<span class="item-property">运营位类型：</span>'+
								'			<span data-elemid="R003">无</span>'+
								'		</div>'+
								'	</div>'+
								'	<div class="row">'+
								'		<div class="col-1">'+
								'			<span class="item-property">运营位目录：</span>'+
								'			<span data-elemid="R004">无</span>'+
								'		</div>'+
								'		<div class="col-1">'+
								'			<span class="item-property" data-elemid="R005">图片：</span>'+
								'			<span>无</span>'+
								'		</div>'+
								'	</div>'+
								'	<div class="row">'+
								'		<span class="item-property">跳转链接：</span>'+
								'		<span data-elemid="R007">无</span>'+
								'	</div>'+
								'	<div class="row">'+
								'		<span class="item-property col-long">营销用语：</span>'+
								'		<span data-elemid="R001" class="col-long-value">无</span>'+
								'	</div>'+
								'	<div class="row approver-box">'+
								'		<div class="col-1">'+
								'			<span class="item-property">一级审批：</span>'+
								'			<span data-id="contentApproverId">无</span>'+
								'		</div>'+
								'		<div class="col-1">'+
								'			<span class="item-property">二级审批：</span>'+
								'			<span data-id="channelApproverId">无</span>'+
								'		</div>'+
								'	</div>'+
								'</li>'
		},
		"q05": {
			channelName: "和生活",
			channelTabNavItem: '<a href="#q05">和生活</a>',
			template: '<!-- 和生活 -->'+
								'<li data-channelid="q05">'+
								'	<div class="row">'+
								'		<span class="item-property">跳转链接：</span>'+
								'		<span data-elemid="R007">无</span>'+
								'	</div>'+
								'	<div class="row">'+
								'		<span class="item-property col-long">图片链接：</span>'+
								'		<span data-elemid="R006" class="col-long-value">无</span>'+
								'	</div>'+
								'	<div class="row approver-box">'+
								'		<div class="col-1">'+
								'			<span class="item-property">一级审批：</span>'+
								'			<span data-id="contentApproverId">无</span>'+
								'		</div>'+
								'		<div class="col-1">'+
								'			<span class="item-property">二级审批：</span>'+
								'			<span data-id="channelApproverId">无</span>'+
								'		</div>'+
								'	</div>'+
								'</li>'
		},
		"q08": {
			channelName: "今日头条",
			channelTabNavItem: '<a href="#q08">今日头条</a>',
			template: '<!-- 今日头条 -->'+
								'<li data-channelid="q08">'+
								'	<div class="row">'+
								'		<span class="item-property">标题：</span>'+
								'		<span data-elemid="R011">无</span>'+
								'	</div>'+
								'	<div class="row">'+
								'		<span class="item-property col-long">图片链接：</span>'+
								'		<span data-elemid="R006" >无</span>'+
								'	</div>'+
								'	<div class="row">'+
								'		<span class="item-property" >跳转链接：</span>'+
								'		<span data-elemid="R007">无</span>'+
								'	</div>'+
								'	<div class="row">'+
								'		<span class="item-property" >点位短代码：</span>'+
								'		<span data-elemid="R020">无</span>'+
								'	</div>'+
								'	<div class="row approver-box">'+
								'		<div class="col-1">'+
								'			<span class="item-property">二级审批：</span>'+
								'			<span data-id="contentApproverId">无</span>'+
								'		</div>'+
								'		<div class="col-1">'+
								'			<span class="item-property">一级审批：</span>'+
								'			<span data-id="channelApproverId">无</span>'+
								'		</div>'+
								'	</div>'+
								'</li>'
		},
        "q10": {
            channelName: "微信公众号",
            channelTabNavItem: '<a href="#q10">微信公众号</a>',
            template: '<!-- 微信公众号 -->'+
            '<li data-channelid="q10">'+
            '	<div class="row approver-box">'+
            '		<div class="col-1">'+
            '			<span class="item-property">配置类型：</span>'+
            '			<span data-elemid="R002">无</span>'+
            '		</div>'+
            '	</div> '+
            '	<div class="row">'+
            '		<span class="item-property">图片链接：</span>'+
            '		<span data-elemid="R006">无</span>'+
            '	</div>'+
            '	<div class="row">'+
            '		<span class="item-property">跳转链接：</span>'+
            '		<span data-elemid="R007">无</span>'+
            '	</div>'+
            '	<div class="row">'+
            '		<span class="item-property">图标链接：</span>'+
            '		<span data-elemid="R019">无</span>'+
            '	</div>'+
            '	<div class="row">'+
            '		<span class="item-property">标题：</span>'+
            '		<span data-elemid="R011">无</span>'+
            '	</div>'+
            '	<div class="row">'+
            '		<span class="item-property">资费代码名称：</span>'+
            '		<span data-elemid="R012">无</span>'+
            '	</div>'+
            '	<div class="row">'+
            '		<span class="item-property">资费订购基数：</span>'+
            '		<span data-elemid="R014">无</span>'+
            '	</div>'+
            '	<div class="row approver-box">'+
            '		<div class="col-1">'+
            '			<span class="item-property">一级审批：</span>'+
            '			<span data-id="contentApproverId">无</span>'+
            '		</div>'+
            '		<div class="col-1">'+
            '			<span class="item-property">二级审批：</span>'+
            '			<span data-id="channelApproverId">无</span>'+
            '		</div>'+
            '	</div>'+
            '</li>'
        },
        "q11": {
            channelName: "微信朋友圈",
            channelTabNavItem: '<a href="#q11">微信朋友圈</a>',
            template: '<!-- 微信朋友圈 -->'+
            '<li data-channelid="q11">'+
            '	<div class="row approver-box">'+
            '		<div class="col-1">'+
            '			<span class="item-property">标题：</span>'+
            '			<span data-elemid="R011">无</span>'+
            '		</div>'+
            '	</div> '+
            '	<div class="row">'+
            '		<span class="item-property">URL：</span>'+
            '		<span data-elemid="R007">无</span>'+
            '	</div>'+
            '	<div class="row">'+
            '		<span class="item-property">广告出价：</span>'+
            '		<span data-elemid="R016">无</span>'+
            '	</div>'+
            '	<div class="row">'+
            '		<span class="item-property">重点城市日限额：</span>'+
            '		<span data-elemid="R017">无</span>'+
            '	</div>'+
            '	<div class="row">'+
            '		<span class="item-property">其他城市日限额：</span>'+
            '		<span data-elemid="R018">无</span>'+
            '	</div>'+
            '	<div class="row">'+
            '		<span class="item-property" data-elemid="R005">图片：</span>'+
            '		<span >无</span>'+
            '	</div>'+
            '	<div class="row approver-box">'+
            '		<div class="col-1">'+
            '			<span class="item-property">一级审批：</span>'+
            '			<span data-id="contentApproverId">无</span>'+
            '		</div>'+
            '		<div class="col-1">'+
            '			<span class="item-property">二级审批：</span>'+
            '			<span data-id="channelApproverId">无</span>'+
            '		</div>'+
            '	</div>'+
            '</li>'
        },
		"q13": {
			channelName: "掌厅push",
			channelTabNavItem: '<a href="#q13">掌厅push</a>',
			template: '<!-- 掌厅push -->'+
			'<li data-channelid="q13">'+
			'	<div class="row approver-box">'+
			'		<div class="col-1">'+
			'			<span class="item-property">标题：</span>'+
			'			<span data-elemid="R011">无</span>'+
			'		</div>'+
			'	</div> '+
			'	<div class="row">'+
			'		<span class="item-property">营销用语：</span>'+
			'		<span data-elemid="R001">无</span>'+
			'	</div>'+
			'	<div class="row">'+
			'		<span class="item-property">跳转链接：</span>'+
			'		<span data-elemid="R007">无</span>'+
			'	</div>'+
			'	<div class="row approver-box">'+
			'		<div class="col-1">'+
			'			<span class="item-property">一级审批：</span>'+
			'			<span data-id="contentApproverId">无</span>'+
			'		</div>'+
			'		<div class="col-1">'+
			'			<span class="item-property">二级审批：</span>'+
			'			<span data-id="channelApproverId">无</span>'+
			'		</div>'+
			'	</div>'+
			'</li>'
		},
        "q12": {
            channelName: "和我看",
            channelTabNavItem: '<a href="#q12">和我看</a>',
            template: '<!-- 和我看 -->'+
            '<li data-channelid="q12">'+
            '	<div class="row approver-box">'+
            '		<div class="col-1">'+
            '			<span class="item-property">广告位：</span>'+
            '			<span data-elemid="R002">无</span>'+
            '		</div>'+
            '	</div> '+
            '	<div class="row">'+
            '		<span class="item-property">图片链接：</span>'+
            '		<span data-elemid="R006">无</span>'+
            '	</div>'+
            '	<div class="row">'+
            '		<span class="item-property">跳转链接：</span>'+
            '		<span data-elemid="R007">无</span>'+
            '	</div>'+
            '	<div class="row approver-box">'+
            '		<div class="col-1">'+
            '			<span class="item-property">一级审批：</span>'+
            '			<span data-id="contentApproverId">无</span>'+
            '		</div>'+
            '		<div class="col-1">'+
            '			<span class="item-property">二级审批：</span>'+
            '			<span data-id="channelApproverId">无</span>'+
            '		</div>'+
            '	</div>'+
            '</li>'
        }, "d08": {
            channelName: "客户经理",
            channelTabNavItem: '<a href="#d08">客户经理</a>',
            template: '<!-- 客户经理 -->'+
            '<li data-channelid="d08">'+
            '	<div class="row">'+
            '		<span class="item-property">营销用语：</span>'+
            '		<span data-elemid="R001">无</span>'+
            '	</div>'+
            '	<div class="row approver-box">'+
            '		<div class="col-1">'+
            '			<span class="item-property">一级审批：</span>'+
            '			<span data-id="contentApproverId">无</span>'+
            '		</div>'+
            '		<div class="col-1">'+
            '			<span class="item-property">二级审批：</span>'+
            '			<span data-id="channelApproverId">无</span>'+
            '		</div>'+
            '	</div>'+
            '</li>'
        }
	},
	detailSceneTab:{
		"2": {
			sceneName: "网厅缴费",
			template: '<!-- 网厅缴费-->'+
			'<div>'+
			'	<div class="sub-row">'+
			'		<span class="item-property">缴费金额：</span>'+
			'		<span data-elemid="SR001">无</span>'+
			'		<span>元 - </span>'+
			'		<span data-elemid="SR002">无</span>'+
			'		<span>元</span>'+
			'	</div>'+
			'	<div class="sub-row">'+
			'		<span class="item-property">缴费渠道：</span>'+
			'		<span data-elemid="SR003">无</span>'+
			'	</div>'+
			'</div>'
		},
		"3": {
			sceneName: "业务订购",
			template: '<!-- 业务订购-->'+
			'<div>'+
			'	<div class="sub-row">'+
			'		<span class="item-property">业务代码：</span>'+
			'		<span data-elemid="SR004">无</span>'+
			'	</div>'+
			'	<div class="sub-row">'+
			'		<span class="item-property">业务名称：</span>'+
			'		<span data-elemid="SR005">无</span>'+
			'	</div>'+
			'</div>'
		},
		"5": {
			sceneName: "上网行为",
			template: '<!-- 上网行为-->'+
			'<div>'+
			'	<div class="sub-row">'+
			'		<span class="item-property">已选APP：</span>'+
			'		<span data-elemid="SR007">无</span>'+
			'	</div>'+
			'</div>'
		},
		"4": {
			sceneName: "实时位置",
			template: '<!-- 实时位置-->'+
			'<div>'+
			'	<div class="sub-row">'+
			'		<span class="item-property">所选位置：</span>'+
			'		<span data-elemid="SR006">无</span>'+
			'	</div>'+
			'</div>'
		},
		"8": {
			sceneName: "PCC场景",
			template: '<!-- PCC场景-->'+
			'<div>'+
			'	<div class="sub-row">'+
			'		<span class="item-property">策略类型：</span>'+
			'		<span data-elemid="SR011">无</span>'+
			'	</div>'+
			'	<div class="sub-row">'+
			'		<span class="item-property">已选内容：</span>'+
			'		<span data-elemid="SR010">无</span>'+
			'	</div>'+
			'</div>'
		},
		"10": {
			sceneName: "饱和度实时场景事件",
			template: '<!-- 饱和度实时场景事件-->'+
			'<div>'+
			'	<div class="sub-row">'+
			'		<span class="item-property">饱和度：</span>'+
			'		<span data-elemid="SR008">无</span>'+
			'		<span>% - </span>'+
			'		<span data-elemid="SR009">无</span>'+
			'		<span>%</span>'+
			'	</div>'+
			'</div>'
		},
		"11": {
			sceneName: "智能地图-实时",
			template: '<!-- 智能地图-->'+
			'<div>'+
			'	<div class="sub-row">'+
			'		<span data-elemid="SR012">无</span>'+
			'	</div>'+
			'	<div class="sub-row j-d-passbyLimitTime">'+
			//'		<span class="item-property">逗留时长：</span>'+
			//'		<span data-elemid="SR013">无</span>'+
			//'		<span>min</span>'+
			'	</div>'+
			'	<div class="sub-row ">'+
			//'		<span class="item-property">用户群计算方式：</span>'+
			//'		<span data-elemid="SR014">无</span>'+
			'	</div>'+
			'</div>'
		},
		"12": {
			sceneName: "流量提醒",
			template: '<!-- 流量提醒-->'+
			'<div>'+
			'	<div class="sub-row">'+
			'		<span class="item-property">提醒类型：</span>'+
			'		<span data-elemid="SR015">无</span>'+
			'	</div>'+
			'</div>'
		},
	}
}