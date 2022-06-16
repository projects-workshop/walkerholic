import axios from "axios"
import { CREATE_PRODUCT_FAIL, CREATE_PRODUCT_REQUEST, CREATE_PRODUCT_SUCCESS, DELETE_PRODUCT_FAIL, DELETE_PRODUCT_REQUEST, DELETE_PRODUCT_SUCCESS, EDIT_PRODUCT_FAIL, EDIT_PRODUCT_REQUEST, EDIT_PRODUCT_SUCCESS, GET_PRODUCTS_FAIL, GET_PRODUCTS_REQUEST, GET_PRODUCTS_SUCCESS, GET_PRODUCT_FAIL, GET_PRODUCT_LIST_FAIL, GET_PRODUCT_LIST_REQUEST, GET_PRODUCT_LIST_SUCCESS, GET_PRODUCT_REQUEST, GET_PRODUCT_SUCCESS, GET_SELLER_PRODUCTS_FAIL, GET_SELLER_PRODUCTS_REQUEST, GET_SELLER_PRODUCTS_SUCCESS } from "../_constants/ProductConstants"

export const getProducts = (page, sort, category, keyword) =>async(dispatch, getState)=>{

    dispatch({
        type:GET_PRODUCTS_REQUEST
    })

    try{
        const res = await axios.get(`/products`,{
            params:{
                // pageRequest : {
                    page,
                    sort : sort? sort :'createdAt',
                // }, 
                category : category ? category :'',
                keyword : keyword ? keyword : ''
            }
        })

        dispatch({
            type:GET_PRODUCTS_SUCCESS,
            payload:res.data
        })

        return res.data.products

    }catch(error){
        dispatch({
            type:GET_PRODUCTS_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}

export const getSellerProducts = (id,page, sort, category,keyword) =>async(dispatch, getState)=>{

    dispatch({
        type:GET_SELLER_PRODUCTS_REQUEST
    })

    try{
        const res = await axios.get(`/users/${id}/products`,{
            params:{
                // pageRequest : {
                    page,
                    sort : sort? sort :'createdAt',
                // }, 
                category : category ? category :'',
                keyword : keyword ? keyword : ''
            }
        })

        dispatch({
            type:GET_SELLER_PRODUCTS_SUCCESS,
            payload:res.data
        })

    }catch(error){
        dispatch({
            type:GET_SELLER_PRODUCTS_FAIL,
            payload:error.response.data
            
        })
    }
}
export const getProduct = (id) =>async(dispatch, getState)=>{

    dispatch({
        type:GET_PRODUCT_REQUEST
    })

    try{
        const res = await axios.get(`/products/${id}`)

        dispatch({
            type:GET_PRODUCT_SUCCESS,
            payload:res.data
        })


    }catch(error){
        dispatch({
            type:GET_PRODUCT_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}
export const getProductList = (page, sort) =>async(dispatch, getState)=>{
    const {auth : {token}} = getState()

    dispatch({
        type:GET_PRODUCT_LIST_REQUEST
    })

    try{
        const res = await axios.get(`/products`,{
            params:{
                // pageRequest : {
                    page : page,
                    sort : sort? sort :'',
                // }, 
                category : '',
                keyword : ''
            }
        },{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:GET_PRODUCT_LIST_SUCCESS,
            payload:res.data
        })


    }catch(error){
        dispatch({
            type:GET_PRODUCT_LIST_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}
export const getSellerProductList = (page, sort, id) =>async(dispatch, getState)=>{
    const {auth : {token}} = getState()

    dispatch({
        type:GET_PRODUCT_LIST_REQUEST
    })

    try{
        const res = await axios.get(`/users/${id}/products`, {
            params:{
                // pageRequest : {
                    page : page,
                    sort : sort? sort :'',
                // }, 
                category : '',
                keyword : ''
            }
        },{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:GET_PRODUCT_LIST_SUCCESS,
            payload:res.data
        })


    }catch(error){
        dispatch({
            type:GET_PRODUCT_LIST_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}
export const deleteProduct = (id) =>async(dispatch, getState)=>{
    const {auth : {token}} = getState()

    dispatch({
        type:DELETE_PRODUCT_REQUEST
    })

    try{
        const res = await axios.delete(`/products/${id}`, {
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:DELETE_PRODUCT_SUCCESS,
            payload:id
        })


    }catch(error){
        dispatch({
            type:DELETE_PRODUCT_FAIL,
            payload:error.response.data
            
        })
    }
}
export const editProduct = (productRequest, deletedImages, images, id) =>async(dispatch, getState)=>{
    const {auth : {token}} = getState()

    dispatch({
        type:EDIT_PRODUCT_REQUEST
    })
    try{
        console.log(deletedImages)

        if(images && images.length > 0){
            const bodyFormData = new FormData()
            images.forEach(image=> bodyFormData.append("multipartFile", image))
            const r = await axios.post(`/products/${id}/product-images`, bodyFormData, {
                headers : {Authorization : `Bearer ${token}`}
            })
            console.log(r)
        }

        if(deletedImages && deletedImages.length>0){
            const bodyFormData = new FormData()
            deletedImages.forEach(image=> bodyFormData.append("deletedImages", image))
            const r = await axios.delete(`/products/${id}/product-images`,{
                data: bodyFormData
            }, {
                headers : {Authorization : `Bearer ${token}`,
                'Content-Type': 'multipart/form-data'},
            })
            console.log(r)
        }

        const res = await axios.put(`/products/${id}`,productRequest,{
            headers : {Authorization : `Bearer ${token}`}
        })

        console.log(res)


        dispatch({
            type:EDIT_PRODUCT_SUCCESS,
            payload:res.data
        })


    }catch(error){
        dispatch({
            type:EDIT_PRODUCT_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}

export const createProduct = (bodyFormData) =>async(dispatch, getState)=>{
    const {auth : {token}} = getState()

    dispatch({
        type:CREATE_PRODUCT_REQUEST
    })

    try{
        const res = await axios.post('/products',bodyFormData,{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:CREATE_PRODUCT_SUCCESS,
            payload:res.data
        })


    }catch(error){
        dispatch({
            type:CREATE_PRODUCT_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}

