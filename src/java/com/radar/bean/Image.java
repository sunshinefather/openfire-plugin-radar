package com.radar.bean;

import java.io.Serializable;

/**
 * @author 上传图片
 */
public class Image implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String imageId;

	// 文件保存地址
	private String saveAdd;

	// 文件后缀
	private String fileSuffix;

	// 文件名
	private String fileName;
	
	//web访问地址
    private String webAdd;
    
	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getSaveAdd() {
		return saveAdd;
	}

	public void setSaveAdd(String saveAdd) {
		this.saveAdd = saveAdd;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
    
	public String getWebAdd() {
		return webAdd;
	}

	public void setWebAdd(String webAdd) {
		this.webAdd = webAdd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((imageId == null) ? 0 : imageId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Image other = (Image) obj;
		if (imageId == null) {
			if (other.imageId != null)
				return false;
		} else if (!imageId.equals(other.imageId))
			return false;
		return true;
	}

}
