import { ADD_TO_CART_FAIL, ADD_TO_CART_REQUEST, ADD_TO_CART_SUCCESS, CANCEL_ORDER_FAIL, CANCEL_ORDER_REQUEST, CANCEL_ORDER_SUCCESS, CHECKOUT, CREATE_CART_FAIL, CREATE_CART_REQUEST, CREATE_CART_SUCCESS, CREATE_ORDER_FAIL, CREATE_ORDER_REQUEST, CREATE_ORDER_SUCCESS, DELETE_CARTITEM_FAIL, DELETE_CARTITEM_REQUEST, DELETE_CARTITEM_SUCCESS, DELETE_ORDERITEM_FAIL, DELETE_ORDERITEM_REQUEST, DELETE_ORDERITEM_SUCCESS, GET_CARTITEMS_FAIL, GET_CARTITEMS_REQUEST, GET_CARTITEMS_SUCCESS, GET_ORDER_FAIL, GET_ORDER_REQUEST, GET_ORDER_SUCCESS, UPDATE_ORDERITEM_QTY_FAIL, UPDATE_ORDERITEM_QTY_REQUEST, UPDATE_ORDERITEM_QTY_SUCCESS } from "../_constants/OrderConstants";

export const cartReducer = (state={}, action)=>{
    switch(action.type){
    
        case GET_CARTITEMS_REQUEST:
            return {...state, loading:true, error:""}
        case GET_CARTITEMS_SUCCESS:
            return {...state, loading:false, success:true, ...action.payload, error:""}
        case GET_CARTITEMS_FAIL:
            return {...state, loading:false, error:action.payload}

        case CREATE_CART_REQUEST:
            return {...state, loading:true, error:""}
        case CREATE_CART_SUCCESS:
            return {...state, loading:false, id:action.payload, error:""}
        case CREATE_CART_FAIL:
            return {...state, loading:false, error:action.payload}

        case ADD_TO_CART_REQUEST:
            return {...state, loading:true, error:""}
        case ADD_TO_CART_SUCCESS:
            if(state.items.length === 0){
                return {...state, loading:false, items:[action.payload], error:""}
            }
            else if(state.items.filter(c=>c.id===action.payload.id).length===1){
                return {...state, loading:false, items:state.items.map(o=>o.productId===action.payload.productId? action.payload : o), error:""}
            }else{
                return {...state, loading:false, items:[...state.items, action.payload], error:""}
            }
        case ADD_TO_CART_FAIL:
            return {...state, loading:false, error:action.payload}

        
        case UPDATE_ORDERITEM_QTY_REQUEST:
            return {...state, loading:true, error:""}
        case UPDATE_ORDERITEM_QTY_SUCCESS:
            return {...state, loading:false, items:state.items.map(o=>o.id===action.payload.id? {...o, qty:action.payload.qty}:o), error:""}
        case UPDATE_ORDERITEM_QTY_FAIL:
            return {...state, loading:false, error:action.payload} 
        
        case DELETE_CARTITEM_REQUEST:
            return {...state, loading:true, error:""}
        case DELETE_CARTITEM_SUCCESS:
            return {...state, loading:false, items:state.items.filter(o=>o.id!==action.payload), error:""}
        case DELETE_CARTITEM_FAIL:
            return {...state, loading:false, error:action.payload}
    
        case CHECKOUT:
            return {...state, checkout:true, error:""}

        case CREATE_ORDER_REQUEST:
            return {...state, loading:true, error:""}
        case CREATE_ORDER_SUCCESS:
            return {...state, loading:false, items:[], id:null, checkout:false , error:""}
        case CREATE_ORDER_FAIL:
            return {...state, loading:false, error:action.payload}

        default:
            return state;
    }
}


export const orderReducer = (state={}, action)=>{
    switch(action.type){
        case GET_ORDER_REQUEST:
            return {...state, loading:true, error:""}
        case GET_ORDER_SUCCESS:
            return {...state, loading:false, ...action.payload, error:""}
        case GET_ORDER_FAIL:
            return {...state, loading:false, error:action.payload}

        case CANCEL_ORDER_REQUEST:
            return {...state, loading:true, error:""}
        case CANCEL_ORDER_SUCCESS:
            if(state.id===action.payload.id){
                return {...state, loading:false, orderStatus:action.payload.orderStatus, error:""}
            }else{
                return {...state, loading:false, error:""}
            }
        case CANCEL_ORDER_FAIL:
            return {...state, loading:false, error:action.payload}

        default:
            return state;
    }
}


