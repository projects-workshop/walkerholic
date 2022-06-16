import { AUTH_FAIL, AUTH_REQUEST, AUTH_SUCCESS, GET_AUTH_FOLLOWS, LEVEL_DOWN, LEVEL_UP, LOGIN_FAIL, LOGIN_REQUEST, LOGIN_SUCCESS, LOGOUT_FAIL, LOGOUT_REQUEST, LOGOUT_SUCCESS, REGISTER_FAIL, REGISTER_REQUEST, REGISTER_SUCCESS } from "../_constants/AuthConstants";
import { FOLLOW_FAIL, FOLLOW_REQUEST, FOLLOW_SUCCESS, UNFOLLOW_FAIL, UNFOLLOW_REQUEST, UNFOLLOW_SUCCESS } from "../_constants/FollowConstants";
import { EDIT_PROFILE_FAIL, EDIT_PROFILE_REQUEST, EDIT_PROFILE_SUCCESS } from "../_constants/ProfileConstants";

export const authReducer = (state={}, action)=>{
    switch(action.type){
        case REGISTER_REQUEST:
            return {...state, loading:true, error:""}
        case REGISTER_SUCCESS:
            return {...state, loading:false, user:{...action.payload.user, followings:[], followers:[]}, token:action.payload.token, error:""}
        case REGISTER_FAIL:
            return {...state, loading:false, error:action.payload}

        case LOGIN_REQUEST:
            return {...state, loading:true, error:""}
        case LOGIN_SUCCESS:
            return {...state, loading:false, user:action.payload.user, token:action.payload.token, error:""}
        case LOGIN_FAIL:
            return {...state, loading:false, error:action.payload}

        case LOGOUT_REQUEST:
            return {...state, loading:true, error:""}
        case LOGOUT_SUCCESS:
            return {loading:false, error:""}
        case LOGOUT_FAIL:
            return {...state, loading:false, error:action.payload}
    
        case AUTH_REQUEST:
            return {...state, loading:true, error:""}
        case AUTH_SUCCESS:
            return {...state, loading:false, user:action.payload.user, token:action.payload.token, error:""}
        case AUTH_FAIL:
            return {...state, loading:false, error:action.payload}
    
        case EDIT_PROFILE_REQUEST:
            return {...state, loading:true, error:""}
        case EDIT_PROFILE_SUCCESS:
            return {...state, loading:false, user:action.payload, error:""}
        case EDIT_PROFILE_FAIL:
            return {...state, loading:false, error:action.payload}

        case GET_AUTH_FOLLOWS:
            return {...state, loading:false, user:{...state.user, followers:action.payload.followers, followings:action.payload.followings}, error:""}
    

        case FOLLOW_REQUEST:
            return {...state, loading:true, error:""}
        case FOLLOW_SUCCESS:
            return {...state, loading:false, user:{...state.user, followings: [...state.user.followings,action.payload.follow]}, error:""}
        case FOLLOW_FAIL:
            return {...state, loading:false, error:action.payload}

        case UNFOLLOW_REQUEST:
            return {...state, loading:true, error:""}
        case UNFOLLOW_SUCCESS:
            return {...state, loading:false, user:{...state.user, followings:state.user.followings.filter(follow=> follow.id !== action.payload)}, error:""}
        case UNFOLLOW_FAIL:
            return {...state, loading:false, error:action.payload}
        
        case LEVEL_UP:
            return {...state, user:{...state.user, level:action.payload}, error:""}
        case LEVEL_DOWN:
            return {...state, user:{...state.user, level:action.payload}, error:""}

        
        default:
            return state;
    }
}