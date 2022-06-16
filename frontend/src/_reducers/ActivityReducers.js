import { CREATE_ACTIVITY_FAIL, CREATE_ACTIVITY_REQUEST, CREATE_ACTIVITY_SUCCESS, CREATE_USER_ACTIVITY_FAIL, CREATE_USER_ACTIVITY_REQUEST, CREATE_USER_ACTIVITY_SUCCESS, DELETE_ACTIVITY_FAIL, DELETE_ACTIVITY_REQUEST, DELETE_ACTIVITY_SUCCESS, DELETE_USER_ACTIVITY_FAIL, DELETE_USER_ACTIVITY_REQUEST, DELETE_USER_ACTIVITY_SUCCESS, EDIT_USER_ACTIVITY_FAIL, EDIT_USER_ACTIVITY_REQUEST, EDIT_USER_ACTIVITY_SUCCESS, GET_ACTIVITIES_FAIL, GET_ACTIVITIES_REQUEST, GET_ACTIVITIES_SUCCESS, GET_ACTIVITY_FAIL, GET_ACTIVITY_REQUEST, GET_ACTIVITY_SUCCESS, GET_USER_ACTIVITIES_FAIL, GET_USER_ACTIVITIES_REQUEST, GET_USER_ACTIVITIES_SUCCESS, UPDATE_ACTIVITY_FAIL, UPDATE_ACTIVITY_REQUEST, UPDATE_ACTIVITY_SUCCESS } from "../_constants/ActivityConstants";

export const activityReducer = (state={}, action)=>{
    switch(action.type){
        case GET_ACTIVITIES_REQUEST:
            return {...state, loading:true, error:""}
        case GET_ACTIVITIES_SUCCESS:
            return {...state, loading:false, activities:action.payload, error:""}
        case GET_ACTIVITIES_FAIL:
            return {...state, loading:false, error:action.payload}

        case GET_ACTIVITY_REQUEST:
            return {...state, loading:true, error:""}
        case GET_ACTIVITY_SUCCESS:
            return {...state, loading:false, activity:action.payload, error:""}
        case GET_ACTIVITY_FAIL:
            return {...state, loading:false, error:action.payload}

        case CREATE_ACTIVITY_REQUEST:
            return {...state, loading:true, error:""}
        case CREATE_ACTIVITY_SUCCESS:
            return {...state, loading:false, activities:[...state.activities,action.payload], error:""}
        case CREATE_ACTIVITY_FAIL:
            return {...state, loading:false, error:action.payload}

        case UPDATE_ACTIVITY_REQUEST:
            return {...state, loading:true, error:""}
        case UPDATE_ACTIVITY_SUCCESS:
            return {...state, loading:false, activities: state.activities.map(activity=> activity.id===action.payload.id? action.payload:activity), error:""}
        case UPDATE_ACTIVITY_FAIL:
            return {...state, loading:false, error:action.payload}

        case DELETE_ACTIVITY_REQUEST:
            return {...state, loading:true, error:""}
        case DELETE_ACTIVITY_SUCCESS:
            return {...state, loading:false, activities:state.activities.filter(activity=>activity.id !== action.payload), error:""}
        case DELETE_ACTIVITY_FAIL:
            return {...state, loading:false, error:action.payload}

        case GET_USER_ACTIVITIES_REQUEST:
            return {...state, loading:true, error:""}
        case GET_USER_ACTIVITIES_SUCCESS:
            return {...state, loading:false, userActivity:action.payload, error:""}
        case GET_USER_ACTIVITIES_FAIL:
            return {...state, loading:false, error:action.payload}

        case CREATE_USER_ACTIVITY_REQUEST:
            return {...state, loading:true, error:""}
        case CREATE_USER_ACTIVITY_SUCCESS:
            return {...state, loading:false, userActivity:{...state.userActivity,score: action.payload.finished? state.userActivity.score+action.payload.score : state.userActivity.score,activities:[action.payload,...state.userActivity.activities ]}, error:""}
        case CREATE_USER_ACTIVITY_FAIL:
            return {...state, loading:false, error:action.payload}

        case EDIT_USER_ACTIVITY_REQUEST:
            return {...state, loading:true, error:""}
        case EDIT_USER_ACTIVITY_SUCCESS:
            return {...state, loading:false, userActivity:{...state.userActivity, activities:state.userActivity.activities.map(activity=>activity.id===action.payload.id? action.payload : activity)}, error:""}
        case EDIT_USER_ACTIVITY_FAIL:
            return {...state, loading:false, error:action.payload}

        case DELETE_USER_ACTIVITY_REQUEST:
            return {...state, loading:true, error:""}
        case DELETE_USER_ACTIVITY_SUCCESS:
            return {...state, loading:false, userActivity:{...state.userActivity, score:action.payload.finished?state.userActivity.score-action.payload.score : state.userActivity.score,activities:state.userActivity.activities.filter(activity=> activity.id!==action.payload.id)}, error:""}
        case DELETE_USER_ACTIVITY_FAIL:
            return {...state, loading:false, error:action.payload}

        default:
            return state;
    }
}