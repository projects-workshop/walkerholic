import axios from "axios"
import { CREATE_ACTIVITY_FAIL, CREATE_ACTIVITY_REQUEST, CREATE_ACTIVITY_SUCCESS, CREATE_USER_ACTIVITY_FAIL, CREATE_USER_ACTIVITY_REQUEST, CREATE_USER_ACTIVITY_SUCCESS, DELETE_ACTIVITY_FAIL, DELETE_ACTIVITY_REQUEST, DELETE_ACTIVITY_SUCCESS, DELETE_USER_ACTIVITY_FAIL, DELETE_USER_ACTIVITY_REQUEST, DELETE_USER_ACTIVITY_SUCCESS, EDIT_USER_ACTIVITY_FAIL, EDIT_USER_ACTIVITY_REQUEST, EDIT_USER_ACTIVITY_SUCCESS, GET_ACTIVITIES_FAIL, GET_ACTIVITIES_REQUEST, GET_ACTIVITIES_SUCCESS, GET_ACTIVITY_FAIL, GET_ACTIVITY_REQUEST, GET_ACTIVITY_SUCCESS, GET_USER_ACTIVITIES_FAIL, GET_USER_ACTIVITIES_REQUEST, GET_USER_ACTIVITIES_SUCCESS, UPDATE_ACTIVITY_FAIL, UPDATE_ACTIVITY_REQUEST, UPDATE_ACTIVITY_SUCCESS } from "../_constants/ActivityConstants"

export const getActivities = () =>async(dispatch, getState)=>{

    dispatch({
        type:GET_ACTIVITIES_REQUEST
    })

    try{
        const res = await axios.get('/activities')

        dispatch({
            type:GET_ACTIVITIES_SUCCESS,
            payload:res.data
        })

    }catch(error){
        dispatch({
            type:GET_ACTIVITIES_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message
        })
    }
}

export const getActivity = (id) =>async(dispatch, getState)=>{

    dispatch({
        type:GET_ACTIVITY_REQUEST
    })

    try{
        const res = await axios.get(`/activities/${id}`)

        dispatch({
            type:GET_ACTIVITY_SUCCESS,
            payload:res.data
        })

    }catch(error){
        dispatch({
            type:GET_ACTIVITY_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message
        })
    }
}

export const createActivity = (activityRequest, imageUrl) =>async(dispatch, getState)=>{

    const {auth : {token}} = getState()

    dispatch({
        type:CREATE_ACTIVITY_REQUEST
    })

    try{       
        let res;
        if(imageUrl.name){
            const bodyFormData = new FormData()
            bodyFormData.append('multipartFile', imageUrl)
            await axios.post(`/activities/images`, bodyFormData,{
                headers : {Authorization : `Bearer ${token}`}
            }).then(async(r) =>{
                const uploadedImageUrl = r.data
                activityRequest = {...activityRequest, imageUrl:uploadedImageUrl}   
                 res = await axios.post('/activities',activityRequest,{
                    headers : {Authorization : `Bearer ${token}`}
                })
            }) 
        }else {
            res = await axios.post('/activities',activityRequest,{
            headers : {Authorization : `Bearer ${token}`}
        })
        }
        dispatch({
            type:CREATE_ACTIVITY_SUCCESS,
            payload:res.data
        })

    }catch(error){
        dispatch({
            type:CREATE_ACTIVITY_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}

export const updateActivity = (activityRequest, id, imageUrl) =>async(dispatch, getState)=>{

    const {auth : {token}} = getState()

    dispatch({
        type:UPDATE_ACTIVITY_REQUEST
    })

    try{
        let res;
        if(imageUrl.name){
            const bodyFormData = new FormData()
            bodyFormData.append('multipartFile', imageUrl)
            await axios.post(`/activities/images`, bodyFormData,{
                headers : {Authorization : `Bearer ${token}`}
            }).then(async(r) =>{
                const uploadedImageUrl = r.data
                activityRequest = {...activityRequest, imageUrl:uploadedImageUrl}   
                 res = await axios.put(`/activities/${id}`,activityRequest,{
                    headers : {Authorization : `Bearer ${token}`}
                })
            }) 
        }else {
            res = axios.put(`/activities/${id}`,activityRequest,{
            headers : {Authorization : `Bearer ${token}`}
        })
        }

        dispatch({
            type:UPDATE_ACTIVITY_SUCCESS,
            payload:res.data
        })

    }catch(error){
        dispatch({
            type:UPDATE_ACTIVITY_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}
export const deleteActivity = (id) =>async(dispatch, getState)=>{
    const {auth : {token}} = getState()

    dispatch({
        type:DELETE_ACTIVITY_REQUEST
    })

    try{
        await axios.delete(`/activities/${id}`,{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:DELETE_ACTIVITY_SUCCESS,
            payload:id
        })

    }catch(error){
        dispatch({
            type:DELETE_ACTIVITY_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}



export const getUserActivities = (page) =>async(dispatch, getState)=>{

    const {auth :{user}} = getState()
    const {auth :{token}} = getState()


    dispatch({
        type:GET_USER_ACTIVITIES_REQUEST
    })

    try{
        const res = await axios.get(`/users/${user.id}/user-activities?page=${page}`,{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:GET_USER_ACTIVITIES_SUCCESS,
            payload:res.data
        })

    }catch(error){
        dispatch({
            type:GET_USER_ACTIVITIES_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}

export const createUserActivity = (userActivityRequest) =>async(dispatch, getState)=>{

    const {auth :{token}} = getState()

    dispatch({
        type:CREATE_USER_ACTIVITY_REQUEST
    })

    try{
        const res = await axios.post(`/user-activities`, userActivityRequest,{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:CREATE_USER_ACTIVITY_SUCCESS,
            payload:res.data
        })

        return res.data.level

    }catch(error){
        dispatch({
            type:CREATE_USER_ACTIVITY_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}

export const editUserActivity = (userActivityRequest, id) =>async(dispatch, getState)=>{

    const {auth :{token}} = getState()

    dispatch({
        type:EDIT_USER_ACTIVITY_REQUEST
    })

    try{
        const res = await axios.post(`/user-activities/${id}`, userActivityRequest,{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:EDIT_USER_ACTIVITY_SUCCESS,
            payload:res.data.activity
        })

        return res.data.level

    }catch(error){
        dispatch({
            type:EDIT_USER_ACTIVITY_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}

export const deleteUserActivity = (id, score, finished) =>async(dispatch, getState)=>{

    const {auth :{user}} = getState()
    const {auth :{token}} = getState()

    dispatch({
        type:DELETE_USER_ACTIVITY_REQUEST
    })

    try{
        const res = await axios.delete(`/user-activities/${id}/users/${user.id}`,{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:DELETE_USER_ACTIVITY_SUCCESS,
            payload:{
                id,score,finished
            }
        })

        return res.data

    }catch(error){
        dispatch({
            type:DELETE_USER_ACTIVITY_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}


