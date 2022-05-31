import React from 'react'
import {Link} from 'react-router-dom'
import Exclude from '../../images/Exclude.png'
import Group from '../../images/Group (1).png'

function ProductCard({product}) {
    return (
        <div className="home_product">
            <Link to={`/product/${product.id}`}>
            <div className="home_product_image">
                <img src={product.imagesUrl[0]} alt="" />
                <div className="home_product_image_tool">
                        <img src={Exclude} alt="" />
                </div>
            </div>
            <div className="home_product_name">
                {product.name}
            </div>
            <div className="home_product_price">
                {parseFloat(product.price).toFixed(2)}$
            </div>
            {
                product.stock===0 &&
                <div className="home_product_soldout">
                    <img src={Group} alt="" />
                    <div className="emoji">🍟</div>
                </div>
            }
            </Link>
        </div>
    )
}

export default ProductCard
