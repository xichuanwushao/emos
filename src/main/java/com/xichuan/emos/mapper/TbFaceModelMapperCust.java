package com.xichuan.emos.mapper;


import com.xichuan.emos.domain.TbFaceModel;

public interface TbFaceModelMapperCust {
    public String searchFaceModel(int userId);
    public void insert(TbFaceModel faceModel);
    public int deleteFaceModel(int userId);
}