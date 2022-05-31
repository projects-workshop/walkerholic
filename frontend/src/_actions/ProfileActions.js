import axios from "axios"
import { EDIT_PROFILE_FAIL, EDIT_PROFILE_REQUEST, EDIT_PROFILE_SUCCESS, GET_PROFILE_FAIL, GET_PROFILE_REQUEST, GET_PROFILE_SUCCESS } from "../_constants/ProfileConstants"

export const getProfile = (id) =>async(dispatch, getState)=>{
    const {auth : {token}} = getState()

    dispatch({
        type:GET_PROFILE_REQUEST
    })

    try{
        const res1 = await axios.get(`/user/${id}`,{
            headers : {Authorization : `Bearer ${token}`}
        })
        const res2 = await axios.get(`/follows/${id}`,{
            headers : {Authorization : `Bearer ${token}`}
        })
        const res3 = await axios.get(`/users/${id}/posts`,{
            headers : {Authorization : `Bearer ${token}`}
        })


        dispatch({
            type:GET_PROFILE_SUCCESS,
            payload:{
                user:res1.data,
                followers:res2.data.followers,
                followings:res2.data.followings,
                posts:res3.data.posts,
                likePosts:res3.data.likePosts
            }
        })


    }catch(error){
        dispatch({
            type:GET_PROFILE_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}


export const editProfile = (bodyFormData) =>async(dispatch, getState)=>{
    const {auth : {token}} = getState()

    dispatch({
        type:EDIT_PROFILE_REQUEST
    })

    try{
        const res = await axios.post(`/user/save`,bodyFormData,{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:EDIT_PROFILE_SUCCESS,
            payload:res.data
        })

        return res.data.id

    }catch(error){
        dispatch({
            type:EDIT_PROFILE_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}

