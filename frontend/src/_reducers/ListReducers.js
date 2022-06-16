import { CANCEL_ORDER_FAIL, CANCEL_ORDER_REQUEST, CANCEL_ORDER_SUCCESS, DELIVER_ORDER_FAIL, DELIVER_ORDER_REQUEST, DELIVER_ORDER_SUCCESS, GET_ORDER_LIST_FAIL, GET_ORDER_LIST_REQUEST, GET_ORDER_LIST_SUCCESS } from "../_constants/OrderConstants";
import { CREATE_PRODUCT_FAIL, CREATE_PRODUCT_REQUEST, CREATE_PRODUCT_SUCCESS, DELETE_PRODUCT_FAIL, DELETE_PRODUCT_REQUEST, DELETE_PRODUCT_SUCCESS, EDIT_PRODUCT_FAIL, EDIT_PRODUCT_REQUEST, EDIT_PRODUCT_SUCCESS, GET_PRODUCT_LIST_FAIL, GET_PRODUCT_LIST_REQUEST, GET_PRODUCT_LIST_SUCCESS } from "../_constants/ProductConstants";
import { DELETE_USER_FAIL, DELETE_USER_REQUEST, DELETE_USER_SUCCESS, GET_USER_LIST_FAIL, GET_USER_LIST_REQUEST, GET_USER_LIST_SUCCESS } from "../_constants/UserConstants";

export const listReducer = (state={}, action)=>{
    switch(action.type){
        case GET_ORDER_LIST_REQUEST:
            return {...state, loading:true, error:""}
        case GET_ORDER_LIST_SUCCESS:
            return {...state, loading:false, orders:action.payload, error:""}
        case GET_ORDER_LIST_FAIL:
            return {...state, loading:false, error:action.payload}

        case CANCEL_ORDER_REQUEST:
            return {...state, loading:true, error:""}
        case CANCEL_ORDER_SUCCESS:
            return {...state, loading:false, orders:state.orders?.orders.map(order=>order.id ===action.payload.id ? action.payload : order), error:""}
        case CANCEL_ORDER_FAIL:
            return {...state, loading:false, error:action.payload}

        case DELIVER_ORDER_REQUEST:
            return {...state, loading:true, error:""}
        case DELIVER_ORDER_SUCCESS:
            return {...state, loading:false, orders:state.orders&& state.orders.map(order=>order.id ===action.payload.id ? action.payload : order), error:""}
        case DELIVER_ORDER_FAIL:
            return {...state, loading:false, error:action.payload}
    
        case GET_USER_LIST_REQUEST:
            return {...state, loading:true, error:""}
        case GET_USER_LIST_SUCCESS:
            return {...state, loading:false, users:action.payload, error:""}
        case GET_USER_LIST_FAIL:
            return {...state, loading:false, error:action.payload}
    
        case DELETE_USER_REQUEST:
            return {...state, loading:true, error:""}
        case DELETE_USER_SUCCESS:
            return {...state, loading:false, users:{...state.users, users:state.users.users.filter(user=> user.id!==action.payload)}, error:""}
        case DELETE_USER_FAIL:
            return {...state, loading:false, error:action.payload}
    
        case GET_PRODUCT_LIST_REQUEST:
            return {...state, loading:true, error:""}
        case GET_PRODUCT_LIST_SUCCESS:
            return {...state, loading:false, products:action.payload, error:""}
        case GET_PRODUCT_LIST_FAIL:
            return {...state, loading:false, error:action.payload}
    
        case DELETE_PRODUCT_REQUEST:
            return {...state, loading:true, error:""}
        case DELETE_PRODUCT_SUCCESS:
            return {...state, loading:false, products:{...state.products, products:state.products.products.filter(product=> product.id!==action.payload)}, error:""}
        case DELETE_PRODUCT_FAIL:
            return {...state, loading:false, error:action.payload}
    
        case EDIT_PRODUCT_REQUEST:
            return {...state, loading:true, error:""}
        case EDIT_PRODUCT_SUCCESS:
            return {...state, loading:false, products:{...state.products, products:state.products.products.map(product=> product.id===action.payload.id ? action.payload : product)}, error:""}
        case EDIT_PRODUCT_FAIL:
            return {...state, loading:false, error:action.payload}
    
        case CREATE_PRODUCT_REQUEST:
            return {...state, loading:true, error:""}
        case CREATE_PRODUCT_SUCCESS:
            return {...state, loading:false, products:{...state.products, products:[...state.products.products, action.payload]}, error:""}
        case CREATE_PRODUCT_FAIL:
            return {...state, loading:false, error:action.payload}

    
        default:
            return state;
    }
}