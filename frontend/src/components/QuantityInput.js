import React from 'react'

function QuantityInput({qty, setQty, stock}) {
    const handleQty = (button) =>{
        if(button==="plus"){
            const plusQty = qty+1
            if(plusQty<= stock){
                return setQty(plusQty)
            }else return 
        }else if(button ==="minus"){
            const minusQty = qty-1
            if(minusQty>=1){
                return setQty(minusQty)
            }else{
                return 
            }
        }else{
            if(button<1){
                return window.alert('Quantity should be more than 1.')
            }else if(button > stock){
                return window.alert('Quantity can not be more than stock.')
            }else{
                return setQty(button)
            }
        }
    }
    return (
        <div className="quantityinput" >
            <span><i className="fas fa-minus" onClick={()=>handleQty("minus")}></i></span>
            <input  type="number" value={qty} onChange={(e)=>handleQty(e.target.value)}/>
            <span><i className="fas fa-plus" onClick={()=>handleQty("plus")}></i></span>
    </div>
    )
}

export default QuantityInput
