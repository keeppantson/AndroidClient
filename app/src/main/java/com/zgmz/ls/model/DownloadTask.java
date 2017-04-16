package com.zgmz.ls.model;

public class DownloadTask {

	public String qu_hua_ma;
	public String page_id;				// 现在处理到哪一页了
	public String apply_type;

    public String page_number;        // 一页中的数量
    public String now_work_page;      // 重复定义
    public String now_work_target;
    public String now_work_target_apply_time;

    public String total_number;         // 一共几页
    public String getTotal_number() {
        return total_number;
    }

    public void setTotal_number(String total_number) {
        this.total_number = total_number;
    }

	public String getQu_hua_ma() {
		return qu_hua_ma;
	}

	public void setQu_hua_ma(String qu_hua_ma) {
		this.qu_hua_ma = qu_hua_ma;
	}

	public String getPage_id() {
		return page_id;
	}

	public void setPage_id(String page_id) {
		this.page_id = page_id;
	}

	public String getApply_type() {
		return apply_type;
	}

	public void setApply_type(String apply_type) {
		this.apply_type = apply_type;
	}

	public String getPage_number() {
		return page_number;
	}

	public void setPage_number(String page_number) {
		this.page_number = page_number;
	}

	public String getNow_work_page() {
		return now_work_page;
	}

	public void setNow_work_page(String now_work_page) {
		this.now_work_page = now_work_page;
	}

	public String getNow_work_target() {
		return now_work_target;
	}

	public void setNow_work_target(String now_work_target) {
		this.now_work_target = now_work_target;
	}

	public String getNow_work_target_apply_time() {
		return now_work_target_apply_time;
	}

	public void setNow_work_target_apply_time(String now_work_target_apply_time) {
		this.now_work_target_apply_time = now_work_target_apply_time;
	}

}
