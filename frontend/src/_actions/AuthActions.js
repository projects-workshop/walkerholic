import { AUTH_FAIL, AUTH_REQUEST, AUTH_SUCCESS, LOGIN_FAIL, LOGIN_REQUEST, LOGIN_SUCCESS, LOGOUT_FAIL, LOGOUT_REQUEST, LOGOUT_SUCCESS, REGISTER_FAIL, REGISTER_REQUEST, REGISTER_SUCCESS } from "../_constants/AuthConstants"
import axios from "axios"

export const register = (bodyFormData) =>async(dispatch, getState)=>{

    dispatch({
        type:REGISTER_REQUEST
    })

    try{
        const res = await axios.post('/signup', bodyFormData)

        dispatch({
            type:REGISTER_SUCCESS,
            payload:res.data
        })

        localStorage.setItem("walkerholic_token", JSON.stringify(res.data.token))

        return res.data.user.id

    }catch(error){
        dispatch({
            type:REGISTER_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}

export const login = ({email, password}) =>async(dispatch, getState)=>{

    dispatch({
        type:LOGIN_REQUEST
    })

    const body = {
        username:email,
        password:password
    }

    try{
        const res = await axios.post('/signin', body)

        dispatch({
            type:LOGIN_SUCCESS,
            payload:res.data
        })

        localStorage.setItem("walkerholic_token", JSON.stringify(res.data.token))

        return res.data.user.id

    }catch(error){
        dispatch({
            type:LOGIN_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}


export const auth = (token) =>async(dispatch, getState)=>{

    dispatch({
        type:AUTH_REQUEST
    })


    try{
        const res = await axios.post(`/authenticate?token=${token}`,null)

        dispatch({
            type:AUTH_SUCCESS,
            payload:res.data
        })

        return res.data.user.id


    }catch(error){
        dispatch({
            type:AUTH_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}


export const logout = () =>async(dispatch, getState)=>{

    dispatch({
        type:LOGOUT_REQUEST
    })

    try{
        dispatch({
            type:LOGOUT_SUCCESS
        })

        localStorage.removeItem("walkerholic_token")

    }catch(error){
        dispatch({
            type:LOGOUT_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}