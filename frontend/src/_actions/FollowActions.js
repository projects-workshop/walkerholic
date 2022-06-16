import axios from "axios"
import { FOLLOW_FAIL, FOLLOW_REQUEST, FOLLOW_SUCCESS, UNFOLLOW_FAIL, UNFOLLOW_REQUEST, UNFOLLOW_SUCCESS } from "../_constants/FollowConstants"

export const follow = (id) =>async(dispatch, getState)=>{

    const {auth : {user}} = getState()
    const {auth : {token}} = getState()

    dispatch({
        type:FOLLOW_REQUEST
    })

    try{
        const res = await axios.post(`/follows?fromId=${user.id}&toId=${id}`,null,{
            headers : {Authorization : `Bearer ${token}`}
        })

        const followed = {id:res.data.id, user:{id:user.id, fullname:user.firstname+user.lastname,imageUrl:user.imageUrl }}
        console.log(res)
        dispatch({
            type:FOLLOW_SUCCESS,
            payload:{
                follow:res.data,
                id:user.id,
                followed: followed
            }
        })


    }catch(error){
        dispatch({
            type:FOLLOW_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}

export const unfollow = (id) =>async(dispatch, getState)=>{

    const {auth : {token}} = getState()

    dispatch({
        type:UNFOLLOW_REQUEST
    })

    try{
        await axios.delete(`/follows/${id}`,{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:UNFOLLOW_SUCCESS,
            payload:id
        })


    }catch(error){
        dispatch({
            type:UNFOLLOW_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}
