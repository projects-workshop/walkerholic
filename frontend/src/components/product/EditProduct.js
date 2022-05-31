import axios from 'axios'
import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { checkPostImage } from '../../utils/CheckImage'
import { imageShow, videoShow} from '../../utils/MediaShow'
import { createProduct, editProduct } from '../../_actions/ProductActions'

function EditProduct({product, setIsEdit, isEdit}) {

    const [name, setName] = useState(product? product.name :'')
    const [description, setDescription] = useState(product? product.description : '')
    const [brand, setBrand] = useState(product? product.brand : '')
    const [category, setCategory] = useState(product? product.category : 'TUMBLER')
    const [stock, setStock] = useState(product? product.stock : 0)
    const [price, setPrice] = useState(product? product.price : 0)
    const [images, setImages] = useState([])
    const [productImages, setProductImages] = useState(product? product.imagesUrl:[])
    const [deletedImages, setDeletedImages] = useState([])


    const auth = useSelector(state => state.auth)

    const dispatch = useDispatch()

    const [categories, setCategories] = useState([])

    const getCategories = async() =>{
        await axios.get('/categories').then(res=>{
            setCategories(res.data)
        })
    }

    useEffect(() => {
        getCategories()
    }, [])


    const handleChangeImages=(e)=>{
        const files = [...e.target.files]
        let newImages= []
        files.forEach(file=>{
            const err = checkPostImage(file)
            if(err) return window.alert(err)
            newImages.push(file)
        })

        setImages([...images, ...newImages])
    }

    const deleteProductImage = (index, image)=>{
        const newArr = [...productImages]
        newArr.splice(index, 1)
        setDeletedImages([...deletedImages, image])
        setProductImages(newArr)
    }

    const deleteImage=(index)=>{
        const newArr = [...images]
        newArr.splice(index, 1)
        setImages(newArr)
    }

    const handleSubmit = (e) =>{
        e.preventDefault();

        const productRequest = {
            name,
            description,
            brand,
            category,
            stock,
            price,
            userId:auth.user.id
        }

        if(product){

            if(productImages.length===0 && images.length===0){
                return window.alert("Please add your photo.")
            }
            dispatch(editProduct(productRequest, deletedImages, images, product.id)).then(res=>(
                setIsEdit(false)
            ))
        }else{

            if(images.length===0){
                return window.alert("Please add your photo.")
            }

            const bodyFormData = new FormData()
            new Blob([JSON.stringify(productRequest)], { type: "application/json" })
            bodyFormData.append("productRequest", new Blob([JSON.stringify(productRequest)], { type: "application/json" }))
            images.forEach(image=> bodyFormData.append("multipartFile", image))
            dispatch(createProduct(bodyFormData)).then(res=>(
                setIsEdit(false)
            ))
        }

    }

    const imageTypeCheck = (image)=>{ 
        const type = image.slice(image.lastIndexOf(".")+1).toLowerCase(); 
        if(type === "jpg" || type === "png" || type === "jpeg" || type === "gif" || type === "bmp"){ 
            return "image"
        }else if(type === "mp4" || type === "avi" || type === "wmv" || type === "mov" ){
            return "video"
        }
    }
    

    return (
        <div className="edit_post" >

            <form onSubmit={handleSubmit} encType="multipart/form-data">
                {
                    product 
                    ? <div className="edit_title">Product {product.id}</div>
                    : <div className="edit_title">Create Product</div>
                }
                <div className="form_group">
                    <label htmlFor="name">Name</label>
                    <textarea type="text" className="form_control" value={name} onChange={e=>setName(e.target.value)} />
                </div>

                <div className="form_group">
                    <label htmlFor="category">Category</label>
                    <select name="category" id="category" onChange={(e)=>setCategory(e.target.value)}>
                    {
                        categories.map((c, index)=>(
                            <option value={c} defaultValue={category===c ? true:false} key={index}>{c}</option>
                        ))
                    }
                    </select>
                </div>

                <div className="form_group">
                    <label htmlFor="brand">Brand</label>
                    <input type="text" className="form_control" value={brand} onChange={e=>setBrand(e.target.value)} />
                </div>

                <div className="form_group">
                    <label htmlFor="stock">Stock</label>
                    <input type="number" className="form_control" value={stock} onChange={e=>setStock(e.target.value)} />
                </div>

                <div className="form_group">
                    <label htmlFor="price">Price</label>
                    <input type="number" step="0.01" className="form_control" value={price} onChange={e=>setPrice(e.target.value)} />
                </div>

                <div className="show_images">
                    {
                        productImages &&
                        productImages.map((image, index)=>(
                            <div key={index} id="file_img">
                                {  
                                    imageTypeCheck(image) === "image"
                                        ? imageShow(image)
                                        : videoShow(image)
                                }
                                <span onClick={()=>deleteProductImage(index, image)}>&times;</span>
                            </div>
                        ))
                    }
                    {

                        images && 
                        images.map((image, index)=>
                            <div key={index} id="file_img">
                                {
                                    image.type
                                        ? <div>
                                            {
                                                image.type.match(/video/i)
                                                ? videoShow(URL.createObjectURL(image))
                                                : imageShow(URL.createObjectURL(image))
                                            }
                                        </div>
                                        : imageTypeCheck(image) === "image"
                                                ? imageShow(image)
                                                : videoShow(image)
                                        // <div>
                                        //     {
                                        //         image.type.match(/video/i)
                                        //         ? videoShow(URL.createObjectURL(image))
                                        //         : imageShow(URL.createObjectURL(image))
                                        //     }
                                        // </div>
                                }
                                <span onClick={()=>deleteImage(index)}>&times;</span>
                            </div>
                        )
                    }
                </div>

                <div className="input_images">
                    <div className="file_upload">
                        <i className="fas fa-image"></i>
                        <input type="file" name="file" id="file_up" multiple accept="image/*, video/*" onChange={handleChangeImages}/>
                    </div>
                </div>

                <div className="form_group">
                    <label htmlFor="description">Description</label>
                    <input type="text" className="form_control" value={description} onChange={e=>setDescription(e.target.value)} />
                </div>

                <div className="form_button">
                    <button type="submit" className="follow_button" style={{marginRight:"1rem"}}>Post</button>
                    <button onClick={()=>setIsEdit(false)} className="unfollow_button">Cancel</button>
                </div>
            </form>
        </div>
    )
}

export default EditProduct
