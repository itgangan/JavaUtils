package com.wanmei.sns.mq.msg;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CreateGroupMsg implements Serializable {
	private static final long serialVersionUID = 1L;

	private long orgid;
	private long waitid;
	private long groupid;
	private long userid; // 创建人
	private String groupName; // 群名
	private String groupIntro; // 群介绍
	private Set<String> memberSet = new HashSet<String>();
	private int groupType; // 0普通群，1官方群
	private int maxMemberNum; // 最大成员数
	private String groupNote; // 群类型
	private long contestid; // 赛事id

	private int count;// 计数器

	public CreateGroupMsg(long orgid, long userid) {
		this.orgid = orgid;
		this.userid = userid;
	}
	
	public synchronized void autoIncrement(){
		this.count++;
	}

	public long getOrgid() {
		return orgid;
	}

	public void setOrgid(long orgid) {
		this.orgid = orgid;
	}

	public long getWaitid() {
		return waitid;
	}

	public void setWaitid(long waitid) {
		this.waitid = waitid;
	}

	public long getGroupid() {
		return groupid;
	}

	public void setGroupid(long groupid) {
		this.groupid = groupid;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupIntro() {
		return groupIntro;
	}

	public void setGroupIntro(String groupIntro) {
		this.groupIntro = groupIntro;
	}

	public Set<String> getMemberSet() {
		return memberSet;
	}

	public void setMemberSet(Set<String> memberSet) {
		this.memberSet = memberSet;
	}

	public int getGroupType() {
		return groupType;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}

	public int getMaxMemberNum() {
		return maxMemberNum;
	}

	public void setMaxMemberNum(int maxMemberNum) {
		this.maxMemberNum = maxMemberNum;
	}

	public String getGroupNote() {
		return groupNote;
	}

	public void setGroupNote(String groupNote) {
		this.groupNote = groupNote;
	}

	public long getContestid() {
		return contestid;
	}

	public void setContestid(long contestid) {
		this.contestid = contestid;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "CreateGroupMsg [orgid=" + orgid + ", waitid=" + waitid + ", groupid=" + groupid + ", userid=" + userid + ", groupName=" + groupName
				+ ", groupIntro=" + groupIntro + ", memberSet=" + memberSet + ", groupType=" + groupType + ", maxMemberNum=" + maxMemberNum
				+ ", groupNote=" + groupNote + ", contestid=" + contestid + "]";
	}

}
