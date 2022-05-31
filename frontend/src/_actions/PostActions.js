import axios from "axios"
import { CREATE_POST_FAIL, CREATE_POST_REQUEST, CREATE_POST_SUCCESS, DELETE_POST_FAIL, DELETE_POST_REQUEST, DELETE_POST_SUCCESS, GET_DISCOVER_POSTS_FAIL, GET_DISCOVER_POSTS_REQUEST, GET_DISCOVER_POSTS_SUCCESS, GET_FOLLOWINGS_POSTS_FAIL, GET_FOLLOWINGS_POSTS_REQUEST, GET_FOLLOWINGS_POSTS_SUCCESS, GET_HOME_POST_FAIL, GET_HOME_POST_REQUEST, GET_HOME_POST_SUCCESS, GET_POST_FAIL, GET_POST_REQUEST, GET_POST_SUCCESS, GET_SEARCH_POST_FAIL, GET_SEARCH_POST_REQUEST, GET_SEARCH_POST_SUCCESS, LIKE_POST_FAIL, LIKE_POST_REQUEST, LIKE_POST_SUCCESS, UNLIKE_POST_FAIL, UNLIKE_POST_REQUEST, UNLIKE_POST_SUCCESS, UPDATE_POST_FAIL, UPDATE_POST_REQUEST, UPDATE_POST_SUCCESS } from "../_constants/PostConstants"

export const getDiscoverPosts = (page, id) =>async(dispatch, getState)=>{

    const {auth : {token}} = getState()

    dispatch({
        type:GET_DISCOVER_POSTS_REQUEST
    })

    try{
        const res = await axios.get(`/users/${id}/posts/discover?page=${page}`,{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:GET_DISCOVER_POSTS_SUCCESS,
            payload:res.data
        })


    }catch(error){
        dispatch({
            type:GET_DISCOVER_POSTS_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}
export const getFollowingsPosts = (page, id) =>async(dispatch, getState)=>{

    const {auth : {token}} = getState()

    dispatch({
        type:GET_FOLLOWINGS_POSTS_REQUEST
    })

    try{
        const res = await axios.get(`/users/${id}/posts/follow?page=${page}`,{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:GET_FOLLOWINGS_POSTS_SUCCESS,
            payload:{
                posts:res.data.posts,
                totalElement:res.data.totalElement,
                totalPage:res.data.totalPage,
                page:page
            }
        })


    }catch(error){
        dispatch({
            type:GET_FOLLOWINGS_POSTS_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message
        })
    }
}

export const createPost = (bodyFormData) =>async(dispatch, getState)=>{
    const {auth : {token}} = getState()

    dispatch({
        type:CREATE_POST_REQUEST
    })

    try{
        const res = await axios.post('/posts', bodyFormData,{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:CREATE_POST_SUCCESS,
            payload:res.data
        })


    }catch(error){
        dispatch({
            type:CREATE_POST_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message
            
        })
    }
}


export const updatePost = (id, postRequest, images, deletedImages) =>async(dispatch, getState)=>{
    const {auth : {token}} = getState()

    dispatch({
        type:UPDATE_POST_REQUEST
    })

    try{

        if(images && images.length > 0){
            const bodyFormData = new FormData()
            images.forEach(image=> bodyFormData.append("multipartFile", image))
            await axios.post(`/posts/${id}/post-images`, bodyFormData, {
                headers : {Authorization : `Bearer ${token}`}
            })
        }

        if(deletedImages && deletedImages.length>0){
            const bodyFormData = new FormData()
            deletedImages.forEach(image=> bodyFormData.append("deletedImages", image))
            await axios.delete(`/posts/${id}/post-images`,{
                data: bodyFormData
            }, {
                headers : {Authorization : `Bearer ${token}`,
                'Content-Type': 'multipart/form-data'},
            })
        }

        const res = await axios.put(`/posts/${id}`, postRequest,{
            headers : {
                Authorization : `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        })

        dispatch({
            type:UPDATE_POST_SUCCESS,
            payload:res.data
        })


    }catch(error){
        dispatch({
            type:UPDATE_POST_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message
            
        })
    }
}


export const deletePost = (id) =>async(dispatch, getState)=>{
    const {auth : {token}} = getState()

    dispatch({
        type:DELETE_POST_REQUEST
    })

    try{
        await axios.delete(`/posts/${id}`,{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:DELETE_POST_SUCCESS,
            payload:id
        })


    }catch(error){
        dispatch({
            type:DELETE_POST_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message
            
        })
    }
}

export const getPost = (id) =>async(dispatch, getState)=>{
    const {auth : {token}} = getState()

    dispatch({
        type:GET_POST_REQUEST
    })

    try{
        const res = await axios.get(`/posts/${id}`,{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:GET_POST_SUCCESS,
            payload:res.data
        })


    }catch(error){
        dispatch({
            type:GET_POST_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message
            
        })
    }
}

export const getHomePost = (page, sort) =>async(dispatch, getState)=>{

    dispatch({
        type:GET_HOME_POST_REQUEST
    })

    // console.log(page)
    try{
        const res = await axios.get(`/posts?page=${page}&sort=${sort}`)

        dispatch({
            type:GET_HOME_POST_SUCCESS,
            payload:{
                posts:res.data.posts,
                totalElement:res.data.totalElement,
                totalPage:res.data.totalPage,
                page:page
            }
        })
        return res.data.posts

    }catch(error){
        dispatch({
            type:GET_HOME_POST_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message
            
        })
    }
}
export const likePost = (postId) =>async(dispatch, getState)=>{
    const {auth : {token}} = getState()
    const {auth: {user}} = getState()

    dispatch({
        type:LIKE_POST_REQUEST
    })

    try{
        const likePostRequest = {
            post: postId,
            user: user.id
        }

        const res = await axios.post('/like-posts', likePostRequest,{
            headers : {Authorization : `Bearer ${token}`}
        })

        // const likePost = {id:res.data.id, userId:res.data.user.id, fullname:res.data.user.fullname, imageUrl:res.data.user.imageUrl? res.data.user.imageUrl : ""}

        
        dispatch({
            type:LIKE_POST_SUCCESS,
            payload:{
                postId:postId,
                likePost:res.data
            }
        })


    }catch(error){
        dispatch({
            type:LIKE_POST_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message
        })

    }
}

export const unlikePost = (postId, id) =>async(dispatch, getState)=>{
    const {auth : {token}} = getState()

    const {auth: {user}} = getState()

    dispatch({
        type:UNLIKE_POST_REQUEST
    })

    try{
        const res = await axios.delete(`/like-posts/${id}`,{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:UNLIKE_POST_SUCCESS,
            payload:{
                likeId:id,
                postId:postId
            }
        })


    }catch(error){
        dispatch({
            type:UNLIKE_POST_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message
            
        })
    }
}

export const getSearchPosts = (page, sort, keyword) =>async(dispatch, getState)=>{

    dispatch({
        type:GET_SEARCH_POST_REQUEST
    })

    try{
        const res = await axios.get(`/posts/search?page=${page}&sort=${sort}&keyword=${keyword}`)

        dispatch({
            type:GET_SEARCH_POST_SUCCESS,
            payload:{
                posts:res.data.posts,
                totalElement:res.data.totalElement,
                totalPage:res.data.totalPage,
                page:page
            }
        })
        console.log(res.data)

    }catch(error){
        dispatch({
            type:GET_SEARCH_POST_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message
            
        })
    }
}