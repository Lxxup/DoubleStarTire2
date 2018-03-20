package com.ryf.weather;

public abstract class AppDialogWrap
{
	private String title = "提示信息";
	private String message = null;
	private String confirmBtn = "确认";
	private String cancelBtn = "取消";
	
	private boolean confirmDialog = false;
	
	public abstract void confirm();
	public abstract void cancel();
	
	public AppDialogWrap(String message) {
		super();
		this.message = message;
	}
	public boolean isConfirmDialog()
	{
		return confirmDialog;
	}
	public void setConfirmDialog(boolean confirmDialog)
	{
		this.confirmDialog = confirmDialog;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getMessage()
	{
		return message;
	}
	public String getConfirmBtn()
	{
		return confirmBtn;
	}
	public void setConfirmBtn(String confirmBtn)
	{
		this.confirmBtn = confirmBtn;
	}
	public String getCancelBtn()
	{
		return cancelBtn;
	}
	public void setCancelBtn(String cancelBtn)
	{
		this.cancelBtn = cancelBtn;
	}
}
